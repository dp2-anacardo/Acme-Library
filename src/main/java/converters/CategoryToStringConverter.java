package converters;

import domain.Category;
import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;

import javax.transaction.Transactional;

@Component
@Transactional
public class CategoryToStringConverter implements Converter<Category,String>{

    @Override
    public String convert(Category sp){
        String result;

        if (sp == null)
            result = null;
        else
            result = String.valueOf(sp.getId());

        return result;
    }
}
