package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psychologist.project.dto.PsychologistDto;
import psychologist.project.service.psychologist.PsychologistService;

@Tag(name = "Psychologist controller",
        description = "Psychologist management endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/psychologists")
public class PsychologistController {
    private final PsychologistService psychologistService;

    @GetMapping("/{id}")
    @Operation(summary = "Get psychologist by id",
            description = "Get psychologist from system by given id")
    public PsychologistDto getAccommodationById(@PathVariable Long id) {
        return psychologistService.getPsychologist(id);
    }
}
