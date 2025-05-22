package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psychologist.project.model.Approach;
import psychologist.project.model.Concern;
import psychologist.project.model.Speciality;
import psychologist.project.service.filterdata.FilterDataService;

@Tag(name = "Filter data controller",
        description = "Data for psychologist filtering management endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/filter-data")
public class FilterDataController {
    private final FilterDataService filterDataService;

    @GetMapping("/concerns")
    @Operation(summary = "Get all concerns",
            description = "Receive all concerns data in system")
    public List<Concern> getAllConcerns() {
        return filterDataService.getAllConcerns();
    }

    @GetMapping("/approaches")
    @Operation(summary = "Get all approaches",
            description = "Receive all approaches data in system")
    public List<Approach> getAllApproach() {
        return filterDataService.getAllApproaches();
    }

    @GetMapping("/specialities")
    @Operation(summary = "Get all specialities",
            description = "Receive all specialities data in system")
    public List<Speciality> getAllSpecialities() {
        return filterDataService.getAllSpecialities();
    }
}
