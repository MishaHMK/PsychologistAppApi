package psychologist.project.service.filterdata;

import java.util.List;
import psychologist.project.model.Approach;
import psychologist.project.model.Concern;
import psychologist.project.model.Speciality;

public interface FilterDataService {
    List<Approach> getAllApproaches();

    List<Speciality> getAllSpecialities();

    List<Concern> getAllConcerns();
}
