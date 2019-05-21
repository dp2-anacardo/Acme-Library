package services;

import domain.Book;
import domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.BookRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class BookService {
    //Manager repository
    @Autowired
    BookRepository bookRepository;

    @Autowired
    ActorService actorService;

    @Autowired
    ReaderService readerService;

    public Book create(){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Book res = new Book();

        return res;
    }

    public Collection<Book> findAll(){
        Collection<Book> res;
        res = this.bookRepository.findAll();
        Assert.notNull(res);
        return res;
    }

    public Book findOne(int bookId){
        Book res;
        res = this.bookRepository.findOne(bookId);
        Assert.notNull(res);
        return res;
    }

    public void delete(Book b){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Assert.notNull(b);
        Assert.isTrue(b.getId() != 0);

        if (haveTransaction(b.getId()) == false)
            this.bookRepository.delete(b);
    }

    public Book save(Book b){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Assert.notNull(b);
        Book res;
        if (b.getId() == 0) {
            int id = this.actorService.getActorLogged().getId();
            Reader r = this.readerService.findOne(id);
            b.setReader(r);
        }
        res = this.bookRepository.save(b);
        return res;
    }

    public Boolean haveTransaction(int bookId){
        Boolean res = false;
        Integer resQ = this.bookRepository.numTransaction(bookId);
        if(resQ > 0)
            res = true;
        return res;
    }

}
