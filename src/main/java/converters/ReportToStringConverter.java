package converters;

import domain.Report;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class ReportToStringConverter implements Converter<Report, String> {

    @Override
    public String convert(final Report sp) {
        String result;

        if (sp == null)
            result = null;
        else
            result = String.valueOf(sp.getId());

        return result;
    }


}
