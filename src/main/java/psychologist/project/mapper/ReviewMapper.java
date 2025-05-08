package psychologist.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.review.CreateReviewDto;
import psychologist.project.dto.review.ReviewDto;
import psychologist.project.model.Review;

@Mapper(config = MapperConfig.class,
        uses = {PsychologistMapper.class, UserMapper.class})
public interface ReviewMapper {
    Review toEntity(CreateReviewDto reviewDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "psychologist.id", target = "psychologistId")
    ReviewDto toDto(Review review);
}
