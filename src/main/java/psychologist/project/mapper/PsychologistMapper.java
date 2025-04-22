package psychologist.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.CreatePsychologistDto;
import psychologist.project.dto.PsychologistDto;
import psychologist.project.dto.UpdatePsychologistDto;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Speciality;

@Mapper(config = MapperConfig.class)
public interface PsychologistMapper {
    @Mapping(source = "specialityId", target = "speciality", qualifiedByName = "specialityFromId")
    Psychologist toEntity(CreatePsychologistDto createDto);

    PsychologistDto toDto(Psychologist dto);

    @Mapping(source = "specialityId", target = "speciality", qualifiedByName = "specialityFromId")
    void updateFromDto(UpdatePsychologistDto dto,
                       @MappingTarget Psychologist psychologist);

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
