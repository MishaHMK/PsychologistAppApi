package psychologist.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.booking.BookingDto;
import psychologist.project.dto.booking.BookingWithPsychologistInfoDto;
import psychologist.project.dto.booking.CreateBookingDto;
import psychologist.project.model.Booking;

@Mapper(config = MapperConfig.class,
        uses = {PsychologistMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "psychologist.id", target = "psychologistId")
    BookingDto toDto(Booking booking);

    @Mapping(source = "userId", target = "user",
            qualifiedByName = "userFromId")
    @Mapping(source = "psychologistId", target = "psychologist",
            qualifiedByName = "psychologistFromId")
    Booking toEntity(CreateBookingDto bookingDto);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userFromId")
    @Mapping(source = "psychologistDto", target = "psychologist")
    Booking toEntity(BookingWithPsychologistInfoDto dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "psychologist", target = "psychologistDto")
    BookingWithPsychologistInfoDto toDetailedDto(Booking booking);
}
