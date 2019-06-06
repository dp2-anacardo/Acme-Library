package converters;

import domain.Offer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class OfferToStringConverter implements Converter<Offer, String> {

    @Override
    public String convert(final Offer sp) {
        String result;

        if (sp == null)
            result = null;
        else
            result = String.valueOf(sp.getId());

        return result;
    }


}
