
package service;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;

import domain.Book;
import services.BookService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BookTest extends AbstractTest {

	@Autowired
	private BookService bookService;


	/*
	 * Testing functional requirement : 31.1 An actor who is authenticated as reader must be able to manage their books
	 * Positive: A reader create a book
	 * Negative: An organizer try create a book
	 * Sentence coverage: 96%
	 * Data coverage: 10%
	 */
	@Test
	public void createBookDriver() {
		final Object testingData[][] = {
			{
				"title", "author", "publisher", "español", "description", 100, "VERY GOOD", "9781234567897", "http://www.photo.com", "reader1", null
			}, {
				"title", "author", "publisher", "español", "description", 100, "VERY GOOD", "9781234567897", "http://www.photo.com", "organizer1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.createBookTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	private void createBookTemplate(final String title, final String author, final String publisher, final String language, final String description, final int pageNumber, final String status, final String isbn, final String photo, final String user,
		final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			Book book = this.bookService.create();

			book.setTitle(title);
			book.setAuthor(author);
			book.setPublisher(publisher);
			book.setLanguageB(language);
			book.setDescription(description);
			book.setPageNumber(pageNumber);
			book.setStatus(status);
			book.setIsbn(isbn);
			book.setPhoto(photo);

			final DataBinder binding = new DataBinder(new Book());
			book = this.bookService.reconstruct(book, binding.getBindingResult());
			this.bookService.save(book);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * Testing functional requirement : 31.1 An actor who is authenticated as reader must be able to manage their books
	 * Positive: A reader edit a book
	 * Negative: An organizer try edit a book
	 * Sentence coverage: 96%
	 * Data coverage: 10%
	 */
	@Test
	public void editBookDriver() {
		final Object testingData[][] = {
			{
				"book3", "prueba", "reader1", null
			}, {
				"book3", "prueba", "organizer1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.editBookTemplate(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void editBookTemplate(final int entityId, final String title, final String user, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {
			this.authenticate(user);
			Book book = this.bookService.findOne(entityId);
			book.setTitle(title);
			final DataBinder binding = new DataBinder(new Book());
			book = this.bookService.reconstruct(book, binding.getBindingResult());
			this.bookService.save(book);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
