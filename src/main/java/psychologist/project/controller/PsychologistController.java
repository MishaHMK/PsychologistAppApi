package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import psychologist.project.dto.psychologist.CreatePsychologistDto;
import psychologist.project.dto.psychologist.PagedPsychologistDto;
import psychologist.project.dto.psychologist.PsychologistDto;
import psychologist.project.dto.psychologist.PsychologistFilterDto;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;
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
    public PsychologistWithDetailsDto getPsychologistsById(@PathVariable Long id) {
        return psychologistService.getPsychologist(id);
    }

    @GetMapping()
    @Operation(summary = "Get all psychologists",
            description = "Get all psychologists from system")
    public List<PsychologistWithDetailsDto> getAllPsychologists(
            @ParameterObject Pageable pageable) {
        return psychologistService.getAllPsychologists(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    @Operation(summary = "Create new psychologist",
            description = "Create psychologist with given data")
    public PsychologistDto createPsychologist(
            @Valid @RequestBody CreatePsychologistDto createDto) {
        return psychologistService.save(createDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete psychologist",
            description = "Mark psychologist found by id as deleted")
    public void deletePsychologist(
            @PathVariable Long id) {
        psychologistService.delete(id);
    }

    @GetMapping("/filter")
    @Operation(summary = "Search for psychologist",
            description = "Filter psychologist data by given params")
    public PagedPsychologistDto filterPsychologists(
            @ParameterObject PsychologistFilterDto filter,
            @ParameterObject Pageable pageable) {
        return psychologistService.search(filter, pageable);
    }

    @PatchMapping("/like/{id}")
    @Operation(summary = "Like psychologist by id",
            description = "Like psychologist by id / Remove like")
    public PsychologistWithDetailsDto likePsychologist(
            @PathVariable Long id) {
        return psychologistService.likePsychologist(id);
    }

    @GetMapping("/liked")
    @Operation(summary = "Get all liked psychologist",
            description = "Get all liked psychologists by logged in user")
    public PagedPsychologistDto getAllLikedPsychologists(
            @ParameterObject Pageable pageable) {
        return psychologistService.getAllLikedPsychologists(pageable);
    }
}
