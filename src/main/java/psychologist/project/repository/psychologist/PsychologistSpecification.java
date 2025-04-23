package psychologist.project.repository.psychologist;

import org.springframework.data.jpa.domain.Specification;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Psychologist.Gender;

import java.math.BigDecimal;

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