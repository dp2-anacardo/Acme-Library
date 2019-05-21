
package converters;

import domain.Administrator;
import domain.Organizer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrganizerToStringConverter implements Converter<Organizer, String> {

	@Override
	public String convert(final Organizer r) {
		String result;

		if (r == null)
			result = null;
		else
			result = String.valueOf(r.getId());

		return result;
	}
}
