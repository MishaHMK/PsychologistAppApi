package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psychologist.project.dto.booking.BookingDto;
import psychologist.project.dto.booking.BookingWithPsychologistInfoDto;
import psychologist.project.dto.booking.CreateBookingDto;
import psychologist.project.dto.booking.UnauthorizedBookingDto;
import psychologist.project.dto.booking.UpdateBookingStatusDto;
import psychologist.project.service.booking.BookingService;
import psychologist.project.service.payment.PaymentService;

@Tag(name = "Booking controller", description = "Booking management endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final PaymentService paymentService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping()
    @Operation(summary = "Get all bookings",
            description = "Receive all bookings data in system [ADMIN ONLY]")
    public List<BookingWithPsychologistInfoDto> getAllBookings(
            @ParameterObject Pageable pageable) {
        return bookingService.getAll(pageable);
    }

    @GetMapping("/free_spots/{psychologistId}")
    @Operation(summary = "Get all free spots",
            description = "Receive all available dates for "
                    + "psychologist by id on given date")
    public List<LocalDateTime> getAllFreeSpots(
            @PathVariable Long psychologistId,
            @ParameterObject LocalDate selectedDate) {
        return bookingService.findAvailableDateTimes(
                selectedDate, psychologistId);
    }

    @GetMapping("/lockedDates/{psychologistId}")
    @Operation(summary = "Get all locked dates for month",
            description = "Receive all unavailable dates for "
                    + "psychologist by id on given date (YYYY-MM)")
    public List<LocalDate> getAllLockedSpots(
            @PathVariable Long psychologistId,
            @ParameterObject String selectedDate) {
        return bookingService.getAllLockedDates(psychologistId, selectedDate);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{psychologistId}")
    @Operation(summary = "Get all today booked meetings",
            description = "Receive all bookings for "
                    + "psychologist by id on given date [ADMIN ONLY]")
    public List<BookingWithPsychologistInfoDto> getAllBookedMeetings(
            @PathVariable Long psychologistId,
            @ParameterObject LocalDate selectedDate) {
        return bookingService.findAllMeetingsForDay(
                selectedDate, psychologistId);
    }

    @GetMapping("/my")
    @Operation(summary = "Get all today booked meetings",
            description = "Receive all bookings of "
                    + "authorized user")
    public List<BookingWithPsychologistInfoDto> getAllMyBookings() {
        return bookingService.findAllMyBookings();
    }

    @PostMapping()
    @Operation(summary = "Make new booking",
            description = "Make new booking with given data")
    public BookingDto makeBooking(
            @Valid @RequestBody CreateBookingDto createBookingDto) {
        return bookingService.createBooking(createBookingDto);
    }

    @PostMapping("/unauthorized")
    @Operation(summary = "Create payment as unauthorized user",
            description = "Create payment entity with session based on booking data")
    public BookingDto makeBookingNotAuth(
            @Valid @RequestBody UnauthorizedBookingDto createBookingDto) {
        return bookingService.createUnauthorizedBooking(createBookingDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/update-status/{bookingId}")
    @Operation(summary = "Update booking status",
            description = "Update booking status by given id [ADMIN ONLY]")
    public BookingDto updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestBody @Valid UpdateBookingStatusDto updateBookingStatusDto) {
        return bookingService.updateBookingStatus(bookingId, updateBookingStatusDto);
    }

    @DeleteMapping("/{bookingId}")
    @Operation(summary = "Cancel booking by id",
            description = "Set booking status cancelled")
    public BookingDto cancelBooking(@PathVariable Long bookingId) {
        paymentService.cancelPaymentForBooking(bookingId);
        return bookingService.setBookingStatusCancelled(bookingId);
    }
}
