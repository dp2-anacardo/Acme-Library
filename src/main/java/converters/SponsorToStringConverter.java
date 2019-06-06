
package converters;

import domain.Sponsor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SponsorToStringConverter implements Converter<Sponsor, String> {

	@Override
	public String convert(final Sponsor s) {
		String result;

		if (s == null)
			result = null;
		else
			result = String.valueOf(s.getId());

		return result;
	}
}
