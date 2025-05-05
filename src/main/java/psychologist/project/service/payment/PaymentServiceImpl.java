package psychologist.project.service.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import psychologist.project.dto.booking.BookingWithPsychologistInfoDto;
import psychologist.project.dto.payment.CreatePaymentDto;
import psychologist.project.dto.payment.PaymentDto;
import psychologist.project.exception.PaymentException;
import psychologist.project.mapper.BookingMapper;
import psychologist.project.mapper.PaymentMapper;
import psychologist.project.model.Booking;
import psychologist.project.model.Payment;
import psychologist.project.repository.bookings.BookingRepository;
import psychologist.project.repository.payments.PaymentsRepository;
import psychologist.project.service.booking.BookingService;
import psychologist.project.utils.StripeUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final String SESSION_COMPLETE_STATUS = "complete";
    public static final String SESSION_OPEN_STATUS = "open";
    public static final String SESSION_EXPIRED_STATUS = "expired";

    private final BookingService bookingService;
    private final StripeUtil stripeUtil;
    private final BookingMapper bookingMapper;
    private final PaymentMapper paymentMapper;
    private final PaymentsRepository paymentsRepository;
    private final BookingRepository bookingsRepository;

    @Override
    public List<PaymentDto> getAll(Pageable pageable) {
        return paymentsRepository.findAll()
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentDto> getAllByUserId(Pageable pageable, Long userId) {
        return paymentsRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentDto save(CreatePaymentDto createPaymentDto) {
        Long bookingId = createPaymentDto.getBookingId();
        Optional<Payment> byBookingId = paymentsRepository.findByBookingId(bookingId);
        if (byBookingId.isPresent()) {
            throw new PaymentException("Payment already exists");
        }
        BookingWithPsychologistInfoDto bookingDetailsById =
                bookingService.getBookingDetailsById(bookingId);
        return createPayment(bookingDetailsById);
    }

    @Override
    public PaymentDto success(String sessionId) {
        try {
            Payment payment = findBySessionId(sessionId);
            if (payment.getStatus().equals(Payment.PaymentStatus.CANCELED)) {
                throw new PaymentException("Payment is already cancelled");
            }
            Session session = stripeUtil.receiveSession(sessionId);
            if (!session.getStatus().equals(SESSION_COMPLETE_STATUS)) {
                throw new PaymentException("Payment with session id: " + sessionId
                        + " is not paid");
            }
            payment.setStatus(Payment.PaymentStatus.PAID);
            return paymentMapper.toDto(payment);
        } catch (StripeException e) {
            throw new PaymentException("Can't find payment session");
        }
    }

    @Override
    public PaymentDto cancel(String sessionId) {
        try {
            Session session = stripeUtil.receiveSession(sessionId);
            if (!session.getStatus().equals(SESSION_OPEN_STATUS)) {
                throw new PaymentException("Payment with session id: " + sessionId
                        + " is not open!");
            }
            Payment payment = findBySessionId(sessionId);
            payment.setStatus(Payment.PaymentStatus.CANCELED);
            return paymentMapper.toDto(payment);
        } catch (StripeException e) {
            throw new PaymentException("Can't find payment session");
        }
    }

    @Override
    public PaymentDto renew(Long paymentId) {
        Payment paymentById = paymentsRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment with id: "
                        + paymentId + " not found"));
        try {
            Session newSession = newSession(paymentById.getAmount());
            paymentById.setSessionId(newSession.getId())
                    .setSessionUrl(new URL(newSession.getUrl()));
            paymentById.setStatus(Payment.PaymentStatus.PENDING);
            paymentsRepository.save(paymentById);
        } catch (MalformedURLException e) {
            throw new PaymentException("Url format is wrong");
        }

        return paymentMapper.toDto(paymentsRepository.save(paymentById));
    }

    private Payment findBySessionId(String sessionId) {
        return paymentsRepository.findBySessionId(sessionId)
                .orElseThrow(
                    () -> new EntityNotFoundException("Payment with session id: "
                        + sessionId + " not found")
        );
    }

    @Scheduled(cron = "0 * * * * *")
    public void markExpiredPayments() {
        List<Payment> paymentStream = paymentsRepository.findAllByStatus(
                Payment.PaymentStatus.PENDING, null)
                .stream()
                .filter(p -> {
                    try {
                        Session session = stripeUtil.receiveSession(p.getSessionId());
                        return session.getStatus().equals(SESSION_EXPIRED_STATUS);
                    } catch (StripeException e) {
                        return false;
                    }
                })
                .peek(p -> p.setStatus(Payment.PaymentStatus.EXPIRED))
                .toList();

        paymentsRepository.saveAll(paymentStream);
    }

    private PaymentDto createPayment(
            BookingWithPsychologistInfoDto bookingData) {
        BigDecimal totalAmount = bookingData
                .getPsychologistDto()
                .getSessionPrice();
        Payment payment = new Payment();
        try {
            Session session = newSession(totalAmount);
            Booking booking = bookingMapper.toEntity(bookingData);
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingsRepository.save(booking);
            payment.setBooking(booking)
                    .setAmount(totalAmount)
                    .setStatus(Payment.PaymentStatus.PENDING)
                    .setSessionId(session.getId())
                    .setSessionUrl(new URL(session.getUrl()));
            return paymentMapper.toDto(paymentsRepository.save(payment));
        } catch (MalformedURLException e) {
            throw new PaymentException("Url format is wrong");
        }
    }

    private Session newSession(BigDecimal totalAmount) {
        try {
            return stripeUtil.createSession(totalAmount, "payment");
        } catch (StripeException e) {
            throw new PaymentException("Can't create payment session");
        }
    }
}
