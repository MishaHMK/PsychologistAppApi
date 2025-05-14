package psychologist.project.dto.payment;

import java.math.BigDecimal;
import java.net.URL;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentPsychologistDto {
    private Long id;
    private Long bookingId;
    private Long psychologistId;
    private String status;
    private String sessionId;
    private URL sessionUrl;
    private BigDecimal amount;
}
