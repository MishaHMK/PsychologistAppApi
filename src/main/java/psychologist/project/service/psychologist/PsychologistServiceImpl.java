package psychologist.project.service.psychologist;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psychologist.project.dto.psychologist.CreatePsychologistDto;
import psychologist.project.dto.psychologist.PsychologistDto;
import psychologist.project.dto.psychologist.PsychologistFilterDto;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;
import psychologist.project.mapper.PsychologistMapper;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Speciality;
import psychologist.project.repository.psychologist.PsychologistRepository;
import psychologist.project.repository.psychologist.PsychologistSpecification;
import psychologist.project.repository.psychologist.SpecialityRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PsychologistServiceImpl implements PsychologistService {
    private final PsychologistRepository psychologistRepository;
    private final PsychologistMapper psychologistMapper;
    private final SpecialityRepository specialityRepository;

    @Override
    public PsychologistWithDetailsDto getPsychologist(Long id) {
        return psychologistMapper.toDetailedDto(
                psychologistRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Psychologist with id "
                                + id + " not found"))
                );
    }

    @Override
    public PsychologistDto save(CreatePsychologistDto createDto) {
        Psychologist toCreate = psychologistMapper.toEntity(createDto);
        if (toCreate.getImageUrl() == null
                || toCreate.getImageUrl().isEmpty()) {
            switch (toCreate.getGender()) {
                case MALE:
                    toCreate.setImageUrl("https://imgur.com/oOFnYdS.png");
                    break;
                case FEMALE:
                    toCreate.setImageUrl("https://imgur.com/gBPz8KZ.png");
                    break;
                default:
                    toCreate.setImageUrl("https://imgur.com/5jWJsCD.png");
                    break;
            }
        }
        PsychologistDto dto = psychologistMapper.toDto(psychologistRepository.save(toCreate));
        return dto.setSpeciality(findSpecialityById(createDto.getSpecialityId()));
    }

    @Override
    public List<PsychologistWithDetailsDto> getAllPsychologists(Pageable pageable) {
        return psychologistRepository.findAll(pageable)
                .stream()
                .map(psychologistMapper::toDetailedDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        psychologistRepository.deleteById(id);
    }

    @Override
    public List<PsychologistWithDetailsDto> search(PsychologistFilterDto filterDto,
                                        Pageable pageable) {
        Specification<Psychologist> spec = Specification.where(null);

        String firstName = filterDto.getFirstName();
        if (firstName != null) {
            spec = spec.and(PsychologistSpecification
                    .hasFirstName(firstName));
        }

        String gender = filterDto.getGender();
        if (gender != null) {
            spec = spec.and(PsychologistSpecification
                    .hasGender(gender));
        }

        Long specialityId = filterDto.getSpecialityId();
        if (specialityId != null) {
            spec = spec.and(PsychologistSpecification
                    .hasSpecialityId(specialityId));
        }

        BigDecimal minPrice = filterDto.getMinPrice();
        if (minPrice != null) {
            spec = spec.and(PsychologistSpecification
                    .hasMinPrice(minPrice));
        }

        BigDecimal maxPrice = filterDto.getMaxPrice();
        if (maxPrice != null) {
            spec = spec.and(PsychologistSpecification
                    .hasMaxPrice(maxPrice));
        }

        Long[] concernIds = filterDto.getConcernIds();
        if (concernIds != null && concernIds.length > 0) {
            spec = spec.and(PsychologistSpecification
                    .hasConcernIds(concernIds));
        }

        Long[] approachIds = filterDto.getApproachIds();
        if (approachIds != null && approachIds.length > 0) {
            spec = spec.and(PsychologistSpecification
                    .hasApproachIds(approachIds));
        }

        return psychologistRepository.findAll(spec, pageable)
                .stream()
                .map(psychologistMapper::toDetailedDto)
                .toList();
    }

    private Speciality findSpecialityById(Long id) {
        return specialityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Speciality with id " + id + " not found")
        );
    }
}
