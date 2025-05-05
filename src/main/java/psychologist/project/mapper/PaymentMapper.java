package psychologist.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.payment.PaymentDto;
import psychologist.project.model.Payment;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(source = "booking.id", target = "bookingId")
    PaymentDto toDto(Payment payment);
}
