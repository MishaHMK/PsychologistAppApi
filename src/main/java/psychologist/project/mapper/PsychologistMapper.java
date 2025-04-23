package psychologist.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.CreatePsychologistDto;
import psychologist.project.dto.PsychologistDto;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Speciality;

@Mapper(config = MapperConfig.class)
public interface PsychologistMapper {
    @Mapping(source = "specialityId", target = "speciality", qualifiedByName = "specialityFromId")
    @Mapping(target = "sessionPrice", source = "sessionPrice")
    Psychologist toEntity(CreatePsychologistDto createDto);

    @Mapping(target = "sessionPrice", source = "sessionPrice")
    PsychologistDto toDto(Psychologist dto);

    @Named("specialityFromId")
    default Speciality specialityFromId(Long id) {
        if (id == null) {
            return null;
        }
        Speciality speciality = new Speciality();
        speciality.setId(id);
        return speciality;
    }
}
