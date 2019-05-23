package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.BookRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class BookService {
    //Manager repository
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Validator validator;

    public Book create() {
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Book res = new Book();

        return res;
    }

    public Collection<Book> findAll() {
        Collection<Book> res;
        res = this.bookRepository.findAll();
        Assert.notNull(res);
        return res;
    }

    public Book findOne(int bookId) {
        Book res;
        res = this.bookRepository.findOne(bookId);
        Assert.notNull(res);
        return res;
    }

    public void delete(Book b) {
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Assert.notNull(b);
        Assert.isTrue(b.getId() != 0);

        if (haveTransaction(b.getId()) == false)
            this.bookRepository.delete(b);
    }

    public void deleteAdmin(Book b) {
        final Actor actor;
        actor = this.actorService.getActorLogged();
        Assert.isTrue(actor instanceof Administrator);
        Assert.notNull(b);

        this.bookRepository.delete(b);
    }


    public Book save(Book b) {
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Assert.notNull(b);
        Book res;
        if (b.getId() == 0) {
            int id = this.actorService.getActorLogged().getId();
            Reader r = this.readerService.findOne(id);
            b.setReader(r);
      //      b.setMoment(new Date());
            if(b.getCategories().isEmpty()){
                Collection<Category> categories = b.getCategories();
                Category defaultCategory = this.categoryService.getDefaultCategory();
                categories.add(defaultCategory);
                b.setCategories(categories);
            }
        }
        res = this.bookRepository.save(b);
        return res;
    }

    public Book reconstruct(Book book, BindingResult binding){
        Book result;
        if(book.getId() == 0){
            result = this.create();
            result.setMoment(new Date());
        } else {
            result = this.bookRepository.findOne(book.getId());
        }

        result.setTitle(book.getTitle());
        result.setAuthor(book.getAuthor());
        result.setPublisher(book.getPublisher());
        result.setLanguage(book.getLanguage());
        result.setDescription(book.getDescription());
        result.setPageNumber(book.getPageNumber());
        result.setStatus(book.getStatus());
        result.setIsbn(book.getIsbn());
        result.setPhoto(book.getPhoto());
        result.setCategories(book.getCategories());
        validator.validate(result, binding);

        if (binding.hasErrors()){
            throw new ValidationException();
        }
        return result;
    }

    public Boolean haveTransaction(int bookId) {
        Boolean res = false;
        Integer resQ = this.bookRepository.numTransaction(bookId);
        if (resQ > 0)
            res = true;
        return res;
    }

    public Collection<Book> getBooksWithNoTransactionsByReader() {
        Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
        return this.bookRepository.getBooksWithNoTransactionsByReader(r);
    }

    public Collection<Book> findAllInactiveBooks(java.sql.Date date) {
        Assert.notNull(date);

        return this.bookRepository.findAllInactiveBooks(date);
    }

    public Collection<Book> getBooksByReader(int readerId){
        Collection<Book> res;
        res = this.bookRepository.getBooksByReader(readerId);
        Assert.notNull(res);
        return res;
    }
}
