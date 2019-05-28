package controllers.book;

import controllers.AbstractController;
import domain.Book;
import domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.BookService;
import services.CategoryService;

import javax.validation.ValidationException;
import java.util.Collection;

@Controller
@RequestMapping("/book")
public class BookController extends AbstractController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private CategoryService categoryService;

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

    @RequestMapping(value = "/reader/create", method = RequestMethod.GET)
    public ModelAndView create(){
        ModelAndView result;
        try{
            Book book = this.bookService.create();
            Collection<Category> categories = this.categoryService.findAll();
            Assert.notNull(categories);
            final String language = LocaleContextHolder.getLocale().getLanguage();
            result = new ModelAndView("book/reader/create");
            result.addObject("book", book);
            result.addObject("categories", categories);
            result.addObject("lang", language);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/reader/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int bookId){
        ModelAndView result;
        try{
            Book book;
            book = this.bookService.findOne(bookId);
            Assert.notNull(book);
            Collection<Category> categories = this.categoryService.findAll();
            Assert.notNull(categories);
            result = new ModelAndView("book/reader/edit");
            result.addObject("book", book);
            result.addObject("categories", categories);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/reader/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(Book book, BindingResult binding){
        ModelAndView result;
        Collection<Category> categories = this.categoryService.findAll();
        try{
            Assert.notNull(categories);
            Assert.notNull(book);
            book = this.bookService.reconstruct(book, binding);
            book = this.bookService.save(book);
            result = new ModelAndView("redirect:list.do");
        }catch (ValidationException e){
            result = this.createEditModelAndView(book, null);
            result.addObject("categories", categories);
        } catch (Throwable oops){
            result = this.createEditModelAndView(book, "book.commit.error");
            result.addObject("categories", categories);
        }
        return result;
    }

    @RequestMapping(value = "/reader/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int bookId){
        ModelAndView result;
        try{
            Book book = this.bookService.findOne(bookId);
            Assert.notNull(book);
            this.bookService.delete(book);
            result = new ModelAndView("redirect:list.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/reader/show", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam int bookId){
        ModelAndView result;
        try{
            Book book = this.bookService.findOne(bookId);
            Assert.notNull(book);
            Collection<Category> categories = book.getCategories();
            Assert.notNull(categories);
            final String language = LocaleContextHolder.getLocale().getLanguage();
            result = new ModelAndView("book/reader/show");
            result.addObject("book", book);
            result.addObject("categories", categories);
            result.addObject("lang", language);
            result.addObject("requestURI", "book/reader/show.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    private ModelAndView createEditModelAndView(final Book book) {
        ModelAndView result;
        result = this.createEditModelAndView(book, null);
        return result;
    }

    private ModelAndView createEditModelAndView(final Book book, final String messageCode) {
        ModelAndView result;

        result = new ModelAndView("book/reader/edit");
        result.addObject("book", book);
        result.addObject("message", messageCode);
        return result;
    }

}
