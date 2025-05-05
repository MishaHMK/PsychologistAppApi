package psychologist.project.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.psychologist.CreatePsychologistDto;
import psychologist.project.dto.psychologist.PsychologistDto;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;
import psychologist.project.model.Approach;
import psychologist.project.model.Concern;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Speciality;

@Mapper(config = MapperConfig.class, uses = {ApproachMapper.class, ConcernMapper.class})
public interface PsychologistMapper {
    @Mapping(source = "approachIds", target = "approaches", qualifiedByName = "approachesByIds")
    @Mapping(source = "concernIds", target = "concerns", qualifiedByName = "concernsByIds")
    @Mapping(source = "specialityId", target = "speciality", qualifiedByName = "specialityFromId")
    @Mapping(target = "sessionPrice", source = "sessionPrice")
    Psychologist toEntity(CreatePsychologistDto createDto);

    @AfterMapping
    default void setApproachIds(@MappingTarget PsychologistDto psychologistDto,
                                Psychologist psychologist) {
        Set<Long> approachIds = psychologist.getApproaches().stream()
                .map(Approach::getId)
                .collect(Collectors.toSet());
        psychologistDto.setApproachIds(approachIds);
    }

    @AfterMapping
    default void setConcernIds(@MappingTarget PsychologistDto psychologistDto,
                                Psychologist psychologist) {
        Set<Long> concernIds = psychologist.getConcerns().stream()
                .map(Concern::getId)
                .collect(Collectors.toSet());
        psychologistDto.setConcernIds(concernIds);
    }

    @Mapping(target = "sessionPrice", source = "sessionPrice")
    PsychologistDto toDto(Psychologist dto);

    PsychologistWithDetailsDto toDetailedDto(Psychologist psychologist);

    @Named("specialityFromId")
    default Speciality specialityFromId(Long id) {
        if (id == null) {
            return null;
        }
        Speciality speciality = new Speciality();
        speciality.setId(id);
        return speciality;
    }

    @Named("psychologistFromId")
    default Psychologist psychologistFromId(Long id) {
        if (id == null) {
            return null;
        }
        Psychologist psychologist = new Psychologist();
        psychologist.setId(id);
        return psychologist;
    }
}
