package converters;

import domain.Offer;
import domain.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class TransactionToStringConverter implements Converter<Transaction, String> {

    @Override
    public String convert(final Transaction sp) {
        String result;

        if (sp == null)
            result = null;
        else
            result = String.valueOf(sp.getId());

        return result;
    }


}
