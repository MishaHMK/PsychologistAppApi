package psychologist.project.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.approach.ApproachDto;
import psychologist.project.model.Approach;

@Mapper(config = MapperConfig.class)
public interface ApproachMapper {
    ApproachDto toDto(Approach approach);

    @Named("approachesByIds")
    default Set<Approach> mapApproachIdsToApproaches(Set<Long> approachIds) {
        if (approachIds == null) {
            return new HashSet<>();
        }
        return approachIds.stream()
                .map(id -> {
                    Approach approach = new Approach();
                    approach.setId(id);
                    return approach;
                })
                .collect(Collectors.toSet());
    }
}
