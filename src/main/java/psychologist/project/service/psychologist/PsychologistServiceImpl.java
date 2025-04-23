package psychologist.project.service.psychologist;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psychologist.project.dto.CreatePsychologistDto;
import psychologist.project.dto.PsychologistDto;
import psychologist.project.dto.PsychologistFilterDto;
import psychologist.project.mapper.PsychologistMapper;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Speciality;
import psychologist.project.repository.psychologist.PsychologistRepository;
import psychologist.project.repository.psychologist.PsychologistSpecification;
import psychologist.project.repository.psychologist.SpecialityRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PsychologistServiceImpl implements PsychologistService {
    private final PsychologistRepository psychologistRepository;
    private final PsychologistMapper psychologistMapper;
    private final SpecialityRepository specialityRepository;

    @Override
    public PsychologistDto getPsychologist(Long id) {
        return psychologistMapper.toDto(
                psychologistRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Psychologist with id "
                                + id + " not found"))
                );
    }

    @Override
    public PsychologistDto save(CreatePsychologistDto createDto) {
        Psychologist toCreate = psychologistMapper.toEntity(createDto);
        PsychologistDto dto = psychologistMapper.toDto(psychologistRepository.save(toCreate));
        return dto.setSpeciality(findSpecialityById(createDto.getSpecialityId()));
    }

    @Override
    public List<PsychologistDto> getAllPsychologists(Pageable pageable) {
        return psychologistRepository.findAll(pageable)
                .stream()
                .map(psychologistMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        psychologistRepository.deleteById(id);
    }

    @Override
    public List<PsychologistDto> search(PsychologistFilterDto filterDto,
                                        Pageable pageable) {
        Specification<Psychologist> spec = Specification.where(null);
        String firstName = filterDto.getFirstName();
        String gender = filterDto.getGender();
        Long specialityId = filterDto.getSpecialityId();
        BigDecimal minPrice = filterDto.getMinPrice();
        BigDecimal maxPrice = filterDto.getMaxPrice();

        if (firstName != null)  {
            spec = spec.and(PsychologistSpecification.
                    hasFirstName(firstName));
        }
        if (gender != null) {
            spec = spec.and(PsychologistSpecification.
                    hasGender(gender));
        }
        if (specialityId != null) {
            spec = spec.and(PsychologistSpecification.
                    hasSpecialityId(specialityId));
        }
        if (minPrice != null) {
            spec = spec.and(PsychologistSpecification.
                    hasMinPrice(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(PsychologistSpecification.
                    hasMaxPrice(maxPrice));
        }

        return psychologistRepository.findAll(spec, pageable)
                .stream()
                .map(psychologistMapper::toDto)
                .toList();
    }

    private Speciality findSpecialityById(Long id) {
        return specialityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Speciality with id " + id + " not found")
        );
    }
}
