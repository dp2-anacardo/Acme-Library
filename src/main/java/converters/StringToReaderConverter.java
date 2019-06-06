
package converters;

import domain.Reader;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.ReaderRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToReaderConverter implements Converter<String, Reader> {

	@Autowired
	ReaderRepository	readerRepository;


	@Override
	public Reader convert(final String text) {
		Reader result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.readerRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
