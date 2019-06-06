package converters;

import domain.Transaction;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.TransactionRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToTransactionConverter implements Converter<String, Transaction> {

    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public Transaction convert(final String text) {
        Transaction result;
        int id;

        try {
            if (StringUtils.isEmpty(text))
                result = null;
            else {
                id = Integer.valueOf(text);
                result = this.transactionRepository.findOne(id);
            }
        } catch (final Throwable oops) {
            throw new IllegalArgumentException(oops);
        }

        return result;
    }
}
