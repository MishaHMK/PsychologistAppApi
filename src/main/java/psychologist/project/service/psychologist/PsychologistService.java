package psychologist.project.service.psychologist;

import java.util.List;
import org.springframework.data.domain.Pageable;
import psychologist.project.dto.CreatePsychologistDto;
import psychologist.project.dto.PsychologistDto;
import psychologist.project.dto.PsychologistFilterDto;

public interface PsychologistService {
    PsychologistDto getPsychologist(Long id);

    PsychologistDto save(CreatePsychologistDto createDto);

    List<PsychologistDto> getAllPsychologists(Pageable pageable);

    List<PsychologistDto> search(PsychologistFilterDto filterDto,
                                 Pageable pageable);

    void delete(Long id);
}
