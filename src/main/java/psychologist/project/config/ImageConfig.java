package psychologist.project.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ImageConfig {
    @Value("${image.male}")
    private String defaultMaleImg;
    @Value("${image.female}")
    private String defaultFemaleImg;
    @Value("${image.other}")
    private String defaultOtherImg;
}
