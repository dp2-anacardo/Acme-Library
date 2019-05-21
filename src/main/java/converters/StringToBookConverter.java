package converters;

import domain.Book;
import domain.Configuration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositories.BookRepository;

@Component
@Transactional
public class StringToBookConverter implements Converter<String, Book> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public Book convert(final String text) {
        Book result;
        int id;

        try {
            if (StringUtils.isEmpty(text))
                result = null;
            else {
                id = Integer.valueOf(text);
                result = this.bookRepository.findOne(id);
            }
        } catch (final Throwable oops) {
            throw new IllegalArgumentException(oops);
        }

        return result;
    }
}
