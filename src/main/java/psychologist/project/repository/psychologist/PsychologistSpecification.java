package psychologist.project.repository.psychologist;

import jakarta.persistence.criteria.Join;
import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import psychologist.project.model.Concern;
import psychologist.project.model.Gender;
import psychologist.project.model.Psychologist;

public class PsychologistSpecification {
    public static Specification<Psychologist> hasFirstName(String firstName) {
        return (root, query, cb) ->
                firstName == null ? null : cb.like(root.get("firstName"), firstName);
    }

    public static Specification<Psychologist> hasGender(String gender) {
        return (root, query, cb) ->
            gender == null ? null : cb.equal(root.get("gender"),
                    Gender.valueOf(gender));
    }

    public static Specification<Psychologist> hasSpecialityId(Long specialityId) {
        return (root, query, cb) ->
                specialityId == null ? null :
                        cb.equal(root.get("speciality").get("id"), specialityId);
    }

    public static Specification<Psychologist> hasConcernIds(Long[] concernIds) {
        return (root, query, cb) -> {
            if (concernIds.length == 0) {
                return null;
            }
            Join<Psychologist, Concern> concernJoin = root.join("concerns");
            return concernJoin.get("id").in(Arrays.asList(concernIds));
        };
    }

    public static Specification<Psychologist> hasApproachIds(Long[] approachIds) {
        return (root, query, cb) ->
                approachIds.length == 0 ? null :
                        root.join("approaches").get("id").in(Arrays.asList(approachIds));
    }

    public static Specification<Psychologist> hasMinPrice(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null :
                        cb.greaterThanOrEqualTo(root.get("sessionPrice"), minPrice);
    }

    public static Specification<Psychologist> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null :
                        cb.lessThanOrEqualTo(root.get("sessionPrice"), maxPrice);
    }
}
