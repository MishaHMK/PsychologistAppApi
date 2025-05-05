package psychologist.project.service.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import psychologist.project.dto.booking.BookingDto;
import psychologist.project.dto.booking.BookingWithPsychologistInfoDto;
import psychologist.project.dto.booking.CreateBookingDto;
import psychologist.project.dto.booking.UpdateBookingStatusDto;

public interface BookingService {
    BookingDto createBooking(CreateBookingDto createDto);

    List<BookingWithPsychologistInfoDto> findAllMyBookings();

    List<BookingWithPsychologistInfoDto> getAll(Pageable pageable);

    List<BookingWithPsychologistInfoDto> findAllMeetingsForDay(
            LocalDate selectedDate, Long psychologistId);

    List<LocalDateTime> findAvailableDateTimes(
            LocalDate selectedDate, Long psychologistId);

    BookingDto setBookingStatusCancelled(Long bookingId);

    BookingDto updateBookingStatus(Long bookingId, UpdateBookingStatusDto updateDto);

    BookingWithPsychologistInfoDto getBookingDetailsById(Long bookingId);
}
