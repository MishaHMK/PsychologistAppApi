package psychologist.project.service.storage;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadImage(MultipartFile file) throws IOException;
}
