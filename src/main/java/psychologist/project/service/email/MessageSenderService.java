package psychologist.project.service.email;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import psychologist.project.dto.booking.BookingWithPsychologistInfoDto;
import psychologist.project.dto.payment.PaymentDto;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;
import psychologist.project.model.Booking;
import psychologist.project.model.User;
import psychologist.project.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class MessageSenderService {
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Value("${backend.url}")
    private String backendUrl;

    @Async
    public void onPaymentCreation(PaymentDto paymentDto,
                                  BookingWithPsychologistInfoDto bookingData) {
        User user = receiveUser(bookingData.getUserId());
        PsychologistWithDetailsDto psychologist = bookingData.getPsychologistDto();
        String html = "<h1>Your payment is ready</h1>"
                + "<p>Hello! "
                + "<p>Your payment has been created for booking</p>"
                + "<p>Date: " + bookingData.getStartTime() + " </p>"
                + "<p>Psychologist: " + psychologist.getFirstName()
                + " " + psychologist.getLastName() + " "
                + psychologist.getFatherName() + " </p>"
                + "<p> You can complete your payment</p>"
                + "<a href=\"" + paymentDto.getSessionUrl()
                + "\">Payment</a>";

        emailService.sendHtmlEmail(user.getEmail(), "Your payment has been created", html);
    }

    @Async
    public void onCancelPayment(BookingWithPsychologistInfoDto bookingData) {
        User user = receiveUser(bookingData.getUserId());
        PsychologistWithDetailsDto psychologist = bookingData.getPsychologistDto();
        String html = "<h1>Your payment is canceled</h1>"
                + "<p>Hello! "
                + "<p>Your payment has been canceled for booking</p>"
                + "<p>Date: " + bookingData.getStartTime() + " </p>"
                + "<p>Psychologist: " + psychologist.getFirstName()
                + " " + psychologist.getLastName() + " "
                + psychologist.getFatherName() + " </p>";

        emailService.sendHtmlEmail(user.getEmail(), "Your payment has been canceled", html);
    }

    @Async
    public void onConfirmPayment(BookingWithPsychologistInfoDto bookingData) {
        User user = receiveUser(bookingData.getUserId());
        PsychologistWithDetailsDto psychologist = bookingData.getPsychologistDto();
        String html = "<h1>Your payment is confirmed</h1>"
                + "<p>Hello! "
                + "<p>Your payment has been accepted for booking</p>"
                + "<p>Date: " + bookingData.getStartTime() + " </p>"
                + "<p>Meeting: " + "<a href=\"" + bookingData.getMeetingUrl()
                + "\">Proceed</a>"
                + "<p>Psychologist: " + psychologist.getFirstName()
                + " " + psychologist.getLastName() + " "
                + psychologist.getFatherName() + " </p>";

        emailService.sendHtmlEmail(user.getEmail(), "Your payment has been confirmed", html);
    }

    @Async
    public void onCreateBooking(Booking bookingData) {
        User user = receiveUser(bookingData.getUser().getId());
        String paymentUrl = backendUrl + "api/payments/create?bookingId=" + bookingData.getId();
        String html = "<h1>Booking created</h1>"
                + "<p>Hello " + user.getFirstName() + ",</p>"
                + "<p>Your meeting has been created</p>"
                + "<p>Date: " + bookingData.getStartTime() + " </p>"
                + "<p>You can confirm this meeting with payment on website"
                + "or in this email by link: "
                + "<a href=\"" + paymentUrl + "\">Create Payment</a>";

        emailService.sendHtmlEmail(user.getEmail(), "Your booking has been created", html);
    }

    private User receiveUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));
    }
}
