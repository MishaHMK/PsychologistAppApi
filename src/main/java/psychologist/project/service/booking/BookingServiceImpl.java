package psychologist.project.service.booking;

import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psychologist.project.config.BookingConfig;
import psychologist.project.dto.booking.BookingDto;
import psychologist.project.dto.booking.BookingWithPsychologistInfoDto;
import psychologist.project.dto.booking.CreateBookingDto;
import psychologist.project.dto.booking.UnauthorizedBookingDto;
import psychologist.project.dto.booking.UpdateBookingStatusDto;
import psychologist.project.exception.AccessException;
import psychologist.project.exception.BookingException;
import psychologist.project.mapper.BookingMapper;
import psychologist.project.model.Booking;
import psychologist.project.model.Psychologist;
import psychologist.project.model.User;
import psychologist.project.repository.bookings.BookingRepository;
import psychologist.project.repository.psychologist.PsychologistRepository;
import psychologist.project.repository.user.UserRepository;
import psychologist.project.security.SecurityUtil;
import psychologist.project.service.psychologist.PsychologistService;
import psychologist.project.service.user.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final PsychologistService psychologistService;
    private final PsychologistRepository psychologistRepository;
    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final BookingConfig config;
    private final UserRepository userRepository;

    @Override
    public List<BookingWithPsychologistInfoDto> findAllMeetingsForDay(
            LocalDate selectedDate, Long psychologistId) {
        return bookingRepository.findAllMeetingsForDay(selectedDate,
                        psychologistId)
                .stream()
                .map(bookingMapper::toDetailedDto)
                .toList();
    }

    @Override
    public List<BookingWithPsychologistInfoDto> findAllMyBookings() {
        Long loggedInUserId = SecurityUtil.getLoggedInUserId();
        return bookingRepository.findAllUserMeetings(loggedInUserId)
                .stream()
                .map(bookingMapper::toDetailedDto)
                .toList();
    }

    @Override
    public List<LocalDateTime> findAvailableDateTimes(
            LocalDate selectedDate, Long psychologistId) {
        List<LocalDateTime> times = receiveMeetingSlots(selectedDate);
        List<LocalDateTime> booked = findAllMeetingsForDay(
                selectedDate, psychologistId)
                .stream()
                .map(BookingWithPsychologistInfoDto::getStartTime)
                .toList();
        times.removeAll(booked);
        return times;
    }

    @Override
    public BookingDto createBooking(CreateBookingDto createDto) {
        Booking toCreate = bookingMapper.toEntity(createDto);
        verifyMeetingPossibility(
                createDto.getStartTime(), createDto.getPsychologistId());
        toCreate.setEndTime(toCreate.getStartTime()
                .plusMinutes(config.getSessionLength()));
        toCreate.setMeetingUrl(psychologistService
                .getPsychologist(
                        createDto.getPsychologistId())
                .getMeetingUrl());
        toCreate.setStatus(Booking.BookingStatus.PENDING);
        bookingRepository.save(toCreate);
        return bookingMapper.toDto(toCreate);
    }

    @Override
    public BookingDto createUnauthorizedBooking(UnauthorizedBookingDto createDto) {
        verifyMeetingPossibilityUnauthorized(
                createDto.getStartTime(), createDto.getPsychologistId());
        Optional<User> unauthorized = userRepository.findByEmail(createDto.getEmail());
        if (unauthorized.isEmpty() || unauthorized.get().getEmail() == null) {
            unauthorized = Optional.of(userService.registerUnauthorized(createDto));
        }
        Psychologist psychologist = psychologistRepository
                .getById(createDto.getPsychologistId());
        Booking toCreate = new Booking()
                .setStartTime(createDto.getStartTime())
                .setUser(unauthorized.get())
                .setEndTime(createDto.getStartTime()
                    .plusMinutes(config.getSessionLength()))
                .setMeetingUrl(psychologist.getMeetingUrl())
                .setPsychologist(psychologist)
                .setEndTime(createDto.getStartTime()
                        .plusMinutes(config.getSessionLength()))
                .setStatus(Booking.BookingStatus.PENDING);
        bookingRepository.save(toCreate);
        return bookingMapper.toDto(toCreate);
    }

    @Override
    public List<BookingWithPsychologistInfoDto> getAll(Pageable pageable) {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toDetailedDto)
                .toList();
    }

    @Override
    public BookingDto updateBookingStatus(Long bookingId, UpdateBookingStatusDto updateDto) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() == Booking.BookingStatus.CANCELED) {
            throw new AccessException("This booking is cancelled");
        }
        booking.setStatus(updateDto.getStatus());
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto setBookingStatusCancelled(Long bookingId) {
        //User loggedInUser = SecurityUtil.getLoggedInUser();
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus().equals(Booking.BookingStatus.EXPIRED)) {
            throw new BookingException("You can't cancel expired booking");
        }
        /*if (!checkAccess(loggedInUser, booking)) {
            throw new AccessException("You can't access this booking");
        }*/
        booking.setStatus(Booking.BookingStatus.CANCELED);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingWithPsychologistInfoDto getBookingDetailsById(Long bookingId) {
        //User loggedInUser = SecurityUtil.getLoggedInUser();
        Booking booking = getBookingById(bookingId);
        /*if (!checkAccess(loggedInUser, booking)) {
            throw new AccessException("You can't access this booking");
        }*/
        return bookingMapper.toDetailedDto(booking);
    }

    @Override
    public List<LocalDate> getAllLockedDates(Long psychologistId, String yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonthValue = YearMonth.parse(yearMonth, formatter);
        LocalDate date = yearMonthValue.atDay(1);
        Month month = date.getMonth();
        int year = date.getYear();

        int daysInMonth = date.getMonth().length(date.isLeapYear());
        return IntStream.rangeClosed(LocalDate.now().getDayOfMonth(), daysInMonth)
                .mapToObj(day -> LocalDate.of(year, month, day))
                .filter(day -> findAvailableDateTimes(day, psychologistId).isEmpty()
                        || (day.getDayOfWeek() == DayOfWeek.SUNDAY
                        || day.getDayOfWeek() == DayOfWeek.SATURDAY))
                .toList();
    }

    @Override
    public List<BookingDto> getAllUserPsychologistsBookings(
            Long userId, Long psychologistId) {

        return bookingRepository.findAllBookingsByUserIdAndPsychologistId(
                userId, psychologistId)
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    private boolean checkAccess(User user, Booking booking) {
        return booking.getUser().getId().equals(user.getId())
                || user.getRole() == User.Role.ADMIN;
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new EntityNotFoundException("Booking with id "
                        + bookingId + " not found"));
    }

    private void verifyMeetingPossibility(
            LocalDateTime dateTime, Long psychologistId) {
        if (!config.getWorkingDays().contains(dateTime.getDayOfWeek())) {
            throw new BookingException("Can't make booking on Saturday or Sunday");
        }

        List<LocalDateTime> allMyBookingTimesForDate =
                getAllMyBookingsTimes(dateTime.toLocalDate());

        if (!allMyBookingTimesForDate.isEmpty()
                && allMyBookingTimesForDate.contains(dateTime)) {
            throw new BookingException("You already have booked meeting "
                    + "for this date and time: "
                    + System.lineSeparator()
                    + makeDateString(dateTime)
            );
        }

        List<LocalDateTime> availableDateTimes =
                findAvailableDateTimes(dateTime.toLocalDate(), psychologistId);
        if (!availableDateTimes.contains(dateTime)) {
            throw new BookingException("This time is not available! "
                    + (availableDateTimes.size() < 9
                    ? "You can still book a meeting for these hours: "
                    + hourList(availableDateTimes)
                    : "There is no available times for this day ("
                    + dateTime.toLocalDate().toString() + ")"));
        }
    }

    private void verifyMeetingPossibilityUnauthorized(
            LocalDateTime dateTime, Long psychologistId) {
        if (!config.getWorkingDays().contains(dateTime.getDayOfWeek())) {
            throw new BookingException("Can't make booking on Saturday or Sunday");
        }

        List<LocalDateTime> availableDateTimes =
                findAvailableDateTimes(dateTime.toLocalDate(), psychologistId);
        if (!availableDateTimes.contains(dateTime)) {
            throw new BookingException("This time is not available! "
                    + (availableDateTimes.size() < 9
                    ? "You can still book a meeting for these hours: "
                    + hourList(availableDateTimes)
                    : "There is no available times for this day ("
                    + dateTime.toLocalDate().toString() + ")"));
        }
    }

    private String hourList(List<LocalDateTime> dates) {
        StringBuilder builder = new StringBuilder();
        for (LocalDateTime date : dates) {
            LocalTime time = date.toLocalTime();
            builder.append(time.getHour())
                    .append(":")
                    .append(time.getMinute())
                    .append("0 ");
        }
        return builder.toString().trim();
    }

    private String makeDateString(LocalDateTime date) {
        StringBuilder builder = new StringBuilder();
        builder.append(date.getMonth())
                .append(" ")
                .append(date.getDayOfMonth())
                .append(", ")
                .append(date.getHour())
                .append(":")
                .append(date.getMinute());
        return builder.toString();
    }

    private List<LocalDateTime> receiveMeetingSlots(
            LocalDate selectedDate) {
        List<LocalDateTime> times = new ArrayList<>();

        for (LocalTime start = config.getStartTime();
                start.isBefore(config.getEndTime());
                start = start.plusMinutes(config.getSlotLength())) {
            times.add(start.atDate(selectedDate));
        }
        return times;
    }

    private List<BookingDto> findAllMyBookingsForDate(
            LocalDate selectedDate) {
        Long loggedInUserId = SecurityUtil.getLoggedInUserId();
        return bookingRepository.findAllUserMeetingsForDate(
                loggedInUserId, selectedDate)
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    private List<LocalDateTime> getAllMyBookingsTimes(
            LocalDate selectedDate) {
        return findAllMyBookingsForDate(selectedDate)
                .stream()
                .map(BookingDto::getStartTime)
                .toList();
    }
}
