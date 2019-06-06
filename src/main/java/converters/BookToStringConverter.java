package converters;

import domain.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class BookToStringConverter implements Converter<Book, String> {
    @Override
    public String convert(final Book configuration) {
        String result;

        if (configuration == null)
            result = null;
        else
            result = String.valueOf(configuration.getId());

        return result;
    }
}
