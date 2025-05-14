package psychologist.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.payment.PaymentDto;
import psychologist.project.dto.payment.PaymentPsychologistDto;
import psychologist.project.model.Payment;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(source = "booking.id", target = "bookingId")
    PaymentDto toDto(Payment payment);

    @Mapping(source = "booking.id", target = "bookingId")
    @Mapping(source = "booking.psychologist.id", target = "psychologistId")
    PaymentPsychologistDto toDetailedDto(Payment payment);
}
