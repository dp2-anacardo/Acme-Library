package converters;

import domain.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.CategoryRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToCategoryConverter implements Converter<String, Category> {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category convert(String text){
        Category result;
        int id;

        try{
            if(StringUtils.isEmpty(text))
                result = null;
            else {
                id = Integer.valueOf(text);
                result = this.categoryRepository.findOne(id);
            }
        } catch (Throwable oops){
            throw new IllegalArgumentException(oops);
        }

        return result;
    }

}
