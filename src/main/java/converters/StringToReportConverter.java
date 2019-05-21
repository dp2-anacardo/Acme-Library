package converters;

import domain.Offer;
import domain.Report;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import repositories.OfferRepository;
import repositories.ReportRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class StringToReportConverter implements Converter<String, Report> {

    @Autowired
    ReportRepository reportRepository;


    @Override
    public Report convert(final String text) {
        Report result;
        int id;

        try {
            if (StringUtils.isEmpty(text))
                result = null;
            else {
                id = Integer.valueOf(text);
                result = this.reportRepository.findOne(id);
            }
        } catch (final Throwable oops) {
            throw new IllegalArgumentException(oops);
        }

        return result;
    }
}
