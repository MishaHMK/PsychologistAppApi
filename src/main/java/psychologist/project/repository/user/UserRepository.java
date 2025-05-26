package psychologist.project.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import psychologist.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdIncludingDeleted(@Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = {"likedPsychologists", "likedPsychologists.speciality"})
    Optional<User> findById(Long id);

    @Modifying
    @Query("UPDATE User u SET u.profileImage = :imgData WHERE u.id = :id")
    void updateUserImage(@Param("id") Long id, @Param("imgData") byte[] imgData);
}
