package converters;

import domain.Register;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class RegisterToString implements Converter<Register,String> {

    @Override
    public String convert(final Register sp) {
        String result;

        if (sp == null)
            result = null;
        else
            result = String.valueOf(sp.getId());

        return result;
    }

}
