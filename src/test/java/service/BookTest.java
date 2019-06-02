package service;


import domain.Book;
import domain.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.BookService;
import services.CategoryService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BookTest extends AbstractTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void createBookDriver(){
        Object testingData[][] = {
                {"title", "author","publisher", "español", "description", 100, "VERY GOOD",
                        "9781234567897", "http://www.photo.com", "reader1", null},
                /*{"title", "author","publisher", "español", "description", 100, "VERY GOOD",
                        "9781234567897", "http://www.photo.com", "organizer1", ValidationException.class}*/
        };
        for(int i =0; i<testingData.length; i++)
            this.createBookTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
        (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (String) testingData[i][6],
                    (String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9],
                    (Class<?>) testingData[i][10]);
    }

    private void createBookTemplate(String title, String author, String publisher,
                                    String language, String description, int pageNumber,
                                    String status, String isbn, String photo,
                                    String user, Class<?> expected){

        Class<?> caught;
        caught = null;

        try{
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

            DataBinder binding = new DataBinder(new Book());
            book = this.bookService.reconstruct(book, binding.getBindingResult());
            this.bookService.save(book);
        } catch (Throwable oops){
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }
}
