package converters;

import domain.Register;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.RegisterRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToRegisterConverter implements Converter<String, Register> {

    @Autowired
    RegisterRepository registerRepository;


    @Override
    public Register convert(final String text) {
        Register result;
        int id;

        try {
            if (StringUtils.isEmpty(text))
                result = null;
            else {
                id = Integer.valueOf(text);
                result = this.registerRepository.findOne(id);
            }
        } catch (final Throwable oops) {
            throw new IllegalArgumentException(oops);
        }

        return result;
    }
}
