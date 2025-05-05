package psychologist.project.service.payment;

import java.util.List;
import org.springframework.data.domain.Pageable;
import psychologist.project.dto.payment.CreatePaymentDto;
import psychologist.project.dto.payment.PaymentDto;

public interface PaymentService {
    List<PaymentDto> getAll(Pageable pageable);

    List<PaymentDto> getAllByUserId(Pageable pageable, Long userId);

    PaymentDto save(CreatePaymentDto createPaymentDto);

    PaymentDto success(String sessionId);

    PaymentDto cancel(String sessionId);

    PaymentDto renew(Long paymentId);
}
