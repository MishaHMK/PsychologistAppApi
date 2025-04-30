package psychologist.project.service.filterdata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psychologist.project.mapper.PsychologistMapper;
import psychologist.project.model.Approach;
import psychologist.project.model.Concern;
import psychologist.project.model.Speciality;
import psychologist.project.repository.psychologist.ApproachRepository;
import psychologist.project.repository.psychologist.ConcernRepository;
import psychologist.project.repository.psychologist.PsychologistRepository;
import psychologist.project.repository.psychologist.SpecialityRepository;

import java.util.List;

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
