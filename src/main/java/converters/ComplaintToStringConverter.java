package converters;

import domain.Complaint;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class ComplaintToStringConverter implements Converter<Complaint, String> {

    @Override
    public String convert(final Complaint sp) {
        String result;

        if (sp == null)
            result = null;
        else
            result = String.valueOf(sp.getId());

        return result;
    }


}
