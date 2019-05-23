package controllers.book;

import controllers.AbstractController;
import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.BookService;

import java.util.Collection;

@Controller
@RequestMapping("/book")
public class BookController extends AbstractController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ActorService actorService;

    @RequestMapping(value = "/reader/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView result;
        try {
            int readerId = this.actorService.getActorLogged().getId();
            Collection<Book> books = this.bookService.getBooksByReader(readerId);
            Assert.notNull(books);
            result = new ModelAndView("book/reader/list");
            result.addObject("books", books);
            result.addObject("requestURI", "book/reader/list.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }
}
