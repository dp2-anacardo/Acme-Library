package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StringToIntegerConverter implements Converter<String,Integer> {
    @Override
    public Integer convert(final String text) {
        Integer result;
        try {
            if (StringUtils.isEmpty(text))
                result = null;
            else
                result = Integer.valueOf(text);

        } catch (final Throwable oops) {
            result = 0;
        }
        return result;
    }
}
