package psychologist.project.dto.psychologist;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import psychologist.project.model.Approach;
import psychologist.project.model.Concern;
import psychologist.project.model.Speciality;

@Data
public class PsychologistWithDetailsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String phoneNumber;
    private String email;
    private String introduction;
    private Speciality speciality;
    private BigDecimal sessionPrice;
    private String gender;
    private Integer experience;
    private String languages;
    private String education;
    private String imageUrl;
    private String meetingUrl;
    private Boolean isLiked;
    private Set<Concern> concerns;
    private Set<Approach> approaches;
}
