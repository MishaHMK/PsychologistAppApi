package psychologist.project.service.psychologist;

import java.util.List;
import org.springframework.data.domain.Pageable;
import psychologist.project.dto.booking.PagedBookingDto;
import psychologist.project.dto.psychologist.CreatePsychologistDto;
import psychologist.project.dto.psychologist.PsychologistDto;
import psychologist.project.dto.psychologist.PsychologistFilterDto;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;

public interface PsychologistService {
    PsychologistWithDetailsDto getPsychologist(Long id);

    PsychologistDto save(CreatePsychologistDto createDto);

    List<PsychologistWithDetailsDto> getAllPsychologists(Pageable pageable);

    PagedBookingDto search(PsychologistFilterDto filterDto,
                           Pageable pageable);

    void delete(Long id);
}
