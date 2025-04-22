package psychologist.project.service.psychologist;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import psychologist.project.dto.CreatePsychologistDto;
import psychologist.project.dto.PsychologistDto;
import psychologist.project.mapper.PsychologistMapper;
import psychologist.project.repository.psychologist.PsychologistRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PsychologistServiceImpl implements PsychologistService {
    private final PsychologistRepository psychologistRepository;
    private final PsychologistMapper psychologistMapper;

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
        return null;
    }

    @Override
    public List<PsychologistDto> getAllPsychologists(Pageable pageable) {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}
