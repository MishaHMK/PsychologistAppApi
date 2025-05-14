package psychologist.project.service.payment;

import java.util.List;
import org.springframework.data.domain.Pageable;
import psychologist.project.dto.payment.CreatePaymentDto;
import psychologist.project.dto.payment.PaymentDto;
import psychologist.project.dto.payment.PaymentPsychologistDto;

public interface PaymentService {
    List<PaymentDto> getAll(Pageable pageable);

    List<PaymentDto> getAllByUserId(Pageable pageable, Long userId);

    PaymentDto save(CreatePaymentDto createPaymentDto);

    PaymentPsychologistDto success(String sessionId);

    PaymentPsychologistDto cancel(String sessionId);

    PaymentDto renew(Long paymentId);

    void cancelPaymentForBooking(Long bookingId);
}
