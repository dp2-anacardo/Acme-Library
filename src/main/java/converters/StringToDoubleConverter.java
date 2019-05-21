package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StringToDoubleConverter implements Converter<String,Double> {
    @Override
    public Double convert(final String text) {
        Double result;
        try {
            if (StringUtils.isEmpty(text))
                result = null;
            else
                result = Double.parseDouble(text);

        } catch (final Throwable oops) {
            result = -1.0;
        }
        return result;
    }
}
