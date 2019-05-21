
package converters;

import domain.Administrator;
import domain.Organizer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.AdministratorRepository;
import repositories.OrganizerRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToOrganizerConverter implements Converter<String, Organizer> {

	@Autowired
	OrganizerRepository organizerRepository;


	@Override
	public Organizer convert(final String text) {
		Organizer result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.organizerRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
