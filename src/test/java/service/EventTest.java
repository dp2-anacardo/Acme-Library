package service;

import domain.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.EventService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Date;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventTest extends AbstractTest {
    @Autowired
    EventService eventService;

    @Test
    public void createDraftEventDriver() {
        final Object testingData[][] = {
                {
                        "prueba", "prueba", new Date(02/02/2025),"prueba",5, "organizer1", null
                }, {
                        "prueba", "prueba", new Date(02/02/2025),"prueba",5, "reader1", IllegalArgumentException.class
        }

        };
        for (int i = 0; i < testingData.length; i++)
            this.createDraftEventTemplate((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (String) testingData[i][3],(int) testingData[i][4], (String) testingData[i][5],  (Class<?>) testingData[i][6]);
    }

    private void createDraftEventTemplate(final String title, final String description, final Date date, String address, int maximumCapacity, String user, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            Event event = this.eventService.create();
            event.setTitle(title);
            event.setDescription(description);
            event.setDate(date);
            event.setAddress(address);
            event.setMaximumCapacity(maximumCapacity);
            final DataBinder binding = new DataBinder(new Event());
            event = this.eventService.reconstruct(event, binding.getBindingResult());
            this.eventService.saveDraft(event);
        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }
}
