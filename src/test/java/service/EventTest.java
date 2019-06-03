
package service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;

import domain.Event;
import services.EventService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventTest extends AbstractTest {

	@Autowired
	EventService eventService;


	/*
	 * Testing functional requirement : 32.1 An actor who is authenticated as organizer must be able to manage their events
	 * Positive: An organizer create an event in draft mode
	 * Negative: A reader try create an event in draft mode
	 * Sentence coverage: 98%
	 * Data coverage: 20%
	 */
	@Test
	public void createDraftEventDriver() {
		final Object testingData[][] = {
			{
				"prueba", "prueba", "02/02/2025 19:00", "prueba", 5, "organizer1", null
			}, {
				"prueba", "prueba", "02/02/2025 19:00", "prueba", 5, "reader1", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.createDraftEventTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void createDraftEventTemplate(final String title, final String description, final String date, final String address, final int maximumCapacity, final String user, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			Event event = this.eventService.create();
			event.setTitle(title);
			event.setDescription(description);
			final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			final Date res = sdf.parse(date);
			event.setDate(res);
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

	/*
	 * Testing functional requirement : 32.1 An actor who is authenticated as organizer must be able to manage their events
	 * Positive: An organizer create an event in final mode
	 * Negative: A reader try create an event in final mode
	 * Sentence coverage: 98%
	 * Data coverage: 20%
	 */
	@Test
	public void createFinalEventDriver() {
		final Object testingData[][] = {
			{
				"prueba", "prueba", "02/02/2025 19:00", "prueba", 5, "organizer1", null
			}, {
				"prueba", "prueba", "02/02/2025 19:00", "prueba", 5, "reader1", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.createFinalEventTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void createFinalEventTemplate(final String title, final String description, final String date, final String address, final int maximumCapacity, final String user, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			Event event = this.eventService.create();
			event.setTitle(title);
			event.setDescription(description);
			final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			final Date res = sdf.parse(date);
			event.setDate(res);
			event.setAddress(address);
			event.setMaximumCapacity(maximumCapacity);
			final DataBinder binding = new DataBinder(new Event());
			event = this.eventService.reconstruct(event, binding.getBindingResult());
			this.eventService.saveFinal(event);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * Testing functional requirement : 32.1 An actor who is authenticated as organizer must be able to manage their events
	 * Positive: An organizer edit an event
	 * Negative: A reader try edit an event. An organizer try edit an event with invalid data.
	 * Sentence coverage: 98%
	 * Data coverage: 66%
	 */
	@Test
	public void editEventDriver() {
		final Object testingData[][] = {
			{
				"event2", "pruebaEdit", "organizer1", null
			}, {
				"event2", "pruebaEdit", "reader1", IllegalArgumentException.class
			}, {
				"event2", "", "organizer1", ValidationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.editEventTemplate(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void editEventTemplate(final int entityId, final String title, final String user, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			Event event = this.eventService.findOne(entityId);
			event.setTitle(title);
			final DataBinder binding = new DataBinder(new Event());
			event = this.eventService.reconstruct(event, binding.getBindingResult());
			this.eventService.saveFinal(event);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Testing functional requirement : 32.1 An actor who is authenticated as organizer must be able to manage their events
	 * Positive: An organizer delete an event
	 * Negative: A reader try delete an event
	 * Sentence coverage: 92%
	 * Data coverage: Not applicable
	 */
	@Test
	public void deleteEventDriver() {
		final Object testingData[][] = {
			{
				"event2", "organizer1", null
			}, {
				"event2", "reader1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.deleteEventTemplate(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void deleteEventTemplate(final int entityId, final String user, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			final Event event = this.eventService.findOne(entityId);
			this.eventService.delete(event);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
