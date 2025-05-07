package psychologist.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE psychologists "
        + "SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Table(name = "psychologists")
public class Psychologist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String fatherName;

    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    private String introduction;

    private Integer experience;

    @Column(nullable = false)
    private String meetingUrl;

    private String imageUrl;

    private String languages;

    private String education;

    @Column(nullable = false)
    private BigDecimal sessionPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "speciality_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Speciality speciality;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "psychologists_concerns",
            joinColumns = @JoinColumn(name = "psychologist_id"),
            inverseJoinColumns = @JoinColumn(name = "concern_id")
    )
    private Set<Concern> concerns = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "psychologists_approaches",
            joinColumns = @JoinColumn(name = "psychologist_id"),
            inverseJoinColumns = @JoinColumn(name = "approach_id")
    )
    private Set<Approach> approaches = new HashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted = false;
}
