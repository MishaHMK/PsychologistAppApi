package psychologist.project.service.filterdata;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psychologist.project.model.Approach;
import psychologist.project.model.Concern;
import psychologist.project.model.Speciality;
import psychologist.project.repository.psychologist.ApproachRepository;
import psychologist.project.repository.psychologist.ConcernRepository;
import psychologist.project.repository.psychologist.SpecialityRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class FilterDataServiceImpl implements FilterDataService {
    private final ConcernRepository concernRepository;
    private final ApproachRepository approachRepository;
    private final SpecialityRepository specialityRepository;

    @Override
    public List<Approach> getAllApproaches() {
        return approachRepository.findAll();
    }

    @Override
    public List<Speciality> getAllSpecialities() {
        return specialityRepository.findAll();
    }

    @Override
    public List<Concern> getAllConcerns() {
        return concernRepository.findAll();
    }
}
