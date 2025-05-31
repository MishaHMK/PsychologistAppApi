package psychologist.project.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import psychologist.project.dto.psychologist.PsychologistFilterDto;

@Component("filterPsychologistKeyGenerator")
public class FilterPsychologistKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        PsychologistFilterDto filter = (PsychologistFilterDto) params[0];
        Pageable pageable = (Pageable) params[1];

        return String.format("filter:%s|%s|%s|%s|%s|concerns:%s"
                        + "|approaches:%s|page:%d|size:%d|sort:%s",
                nullSafe(filter.getFirstName()),
                nullSafe(filter.getGender()),
                nullSafe(filter.getSpecialityId()),
                nullSafe(filter.getMinPrice()),
                nullSafe(filter.getMaxPrice()),
                arrayToString(filter.getConcernIds()),
                arrayToString(filter.getApproachIds()),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().toString());
    }

    private String nullSafe(Object o) {
        return o == null ? "null" : o.toString();
    }

    private String arrayToString(Long[] array) {
        return array == null ? "null" : Arrays.stream(array)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
