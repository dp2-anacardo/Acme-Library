package converters;

import domain.Offer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.OfferRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToOfferConverter implements Converter<String, Offer> {

    @Autowired
    OfferRepository offerRepository;


    @Override
    public Offer convert(final String text) {
        Offer result;
        int id;

        try {
            if (StringUtils.isEmpty(text))
                result = null;
            else {
                id = Integer.valueOf(text);
                result = this.offerRepository.findOne(id);
            }
        } catch (final Throwable oops) {
            throw new IllegalArgumentException(oops);
        }

        return result;
    }
}
