package converters;

import domain.Comment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class CommentToStringConverter implements Converter<Comment, String> {

    @Override
    public String convert(final Comment sp) {
        String result;

        if (sp == null)
            result = null;
        else
            result = String.valueOf(sp.getId());

        return result;
    }


}
