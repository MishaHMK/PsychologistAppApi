package psychologist.project.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.concern.ConcernDto;
import psychologist.project.model.Concern;

@Mapper(config = MapperConfig.class)
public interface ConcernMapper {
    ConcernDto toDto(Concern concern);

    @Named("concernsByIds")
    default Set<Concern> mapConcernIdsToConcerns(Set<Long> concernIds) {
        if (concernIds == null) {
            return new HashSet<>();
        }
        return concernIds.stream()
                .map(id -> {
                    Concern concern = new Concern();
                    concern.setId(id);
                    return concern;
                })
                .collect(Collectors.toSet());
    }
}
