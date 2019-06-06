
package converters;

import domain.Reader;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ReaderToStringConverter implements Converter<Reader, String> {

	@Override
	public String convert(final Reader r) {
		String result;

		if (r == null)
			result = null;
		else
			result = String.valueOf(r.getId());

		return result;
	}
}
