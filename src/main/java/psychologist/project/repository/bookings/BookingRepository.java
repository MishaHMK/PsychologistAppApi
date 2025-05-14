package psychologist.project.repository.bookings;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import psychologist.project.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b "
            + "JOIN FETCH b.psychologist p "
            + "JOIN FETCH p.speciality s "
            + "JOIN FETCH p.approaches "
            + "JOIN FETCH p.concerns "
            + "WHERE EXTRACT(DAY FROM b.startTime) "
            + "= EXTRACT(DAY FROM :selectedDate) "
            + " AND p.id = :psychologistId "
            + " AND (b.status = 'PENDING' "
            + "OR b.status = 'CONFIRMED')")
    List<Booking> findAllMeetingsForDay(LocalDate selectedDate,
                                        Long psychologistId);

    @Query("SELECT b FROM Booking b "
            + "JOIN FETCH b.psychologist p "
            + "JOIN FETCH p.speciality s "
            + "JOIN FETCH p.approaches "
            + "JOIN FETCH p.concerns "
            + "JOIN FETCH b.user u "
            + " WHERE u.id = :userId ")
    List<Booking> findAllUserMeetings(Long userId);

    List<Booking> findAllBookingsByUserIdAndPsychologistId(
            Long userId, Long psychologistId);

    @Query("SELECT b FROM Booking b "
            + "JOIN FETCH b.psychologist p "
            + "JOIN FETCH p.speciality s "
            + "JOIN FETCH p.approaches "
            + "JOIN FETCH p.concerns "
            + "JOIN FETCH b.user u "
            + "WHERE EXTRACT(DAY FROM b.startTime) "
            + "= EXTRACT(DAY FROM :selectedDate) "
            + " AND u.id = :userId "
            + " AND (b.status = 'PENDING' "
            + "OR b.status = 'CONFIRMED')")
    List<Booking> findAllUserMeetingsForDate(
            Long userId, LocalDate selectedDate);

    @Override
    @Query("SELECT b FROM Booking b "
            + "JOIN FETCH b.psychologist p "
            + "JOIN FETCH p.speciality "
            + "JOIN FETCH p.approaches "
            + "JOIN FETCH p.concerns ")
    List<Booking> findAll();
}
