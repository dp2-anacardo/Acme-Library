package services;

import domain.Book;
import domain.Configuration;
import domain.Finder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.FinderRepository;

import java.util.*;

@Service
@Transactional
public class FinderService {

    //Managed repository
    @Autowired
    private FinderRepository finderRepository;

    //Services
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private ReaderService readerService;

    //Validator
    @Autowired
    private Validator validator;


    //Simple CRUD Methods
    public Finder create() {
        Finder result;
        result = new Finder();
        final Collection<Book> books = new ArrayList<>();
        result.setBooks(books);
        result.setLastUpdate(new Date());
        return result;
    }

    public Collection<Finder> findAll() {
        return this.finderRepository.findAll();
    }

    public Finder findOne(final Integer id) {
        return this.finderRepository.findOne(id);
    }

    public Finder save(Finder finder) {

        Assert.notNull(finder);
        if(finder.getId()!=0) {
            Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("READER"));
            Assert.isTrue(this.readerService.findOne(this.actorService.getActorLogged().getId()).getFinder().equals(finder));
        }

        Collection<Book> result = this.search(finder);

        Configuration conf;
        conf = this.configurationService.getConfiguration();

        result = this.maxPosition(result, conf);

        finder.setBooks(result);
        finder.setLastUpdate(new Date());

        finder = this.finderRepository.save(finder);
        return finder;
    }

    //Reconstruct
    public Finder reconstruct(final Finder finder, final BindingResult binding) {
        Finder result;

        result = this.finderRepository.findOne(finder.getId());

        finder.setVersion(result.getVersion());

        result = finder;
        this.validator.validate(finder, binding);

        return result;
    }

    //Another methods

    public void delete(final Finder f) {
        Assert.notNull(f);
        this.finderRepository.delete(f);
    }

    public Collection<Book> getBooksByKeyWord(String keyword) {
        return this.finderRepository.getBooksByKeyWord(keyword);
    }

    public Collection<Book> getBooksContainsKeyWord(String keyword) {
        return this.finderRepository.getBooksContainsKeyWord(keyword);
    }

    public Collection<Book> maxPosition(Collection<Book> result, Configuration configuration) {
        if (result.size() > configuration.getMaxResults()) {

            final List<Book> copy = (List<Book>) result;

            final List<Book> booksLim = new ArrayList<>();
            for (int i = 0; i < configuration.getMaxResults(); i++)
                booksLim.add(copy.get(i));
            result = booksLim;
        }

        return result;
    }

    public Collection<Book> search(Finder finder) {
        Collection<Book> result = Collections.emptyList();
        List<Book> pro1 = null;
        List<Book> pro2 = null;
        List<Book> pro3 = null;
        List<Book> proAux1;
        List<Book> proAux2;

        if (!(finder.getKeyWord() == null || finder.getKeyWord().equals(""))) {
            proAux1 = (List<Book>) this.finderRepository.getBooksByKeyWord(finder.getKeyWord());
            proAux2 = (List<Book>) this.finderRepository.getBooksContainsKeyWord(finder.getKeyWord());

            Set<Book> set = new LinkedHashSet<>(proAux1);
            set.addAll(proAux2);
            pro1 = new ArrayList<>(set);
        }
        if (finder.getCategoryName() != null)
            pro2 = (List<Book>) this.finderRepository.getBooksByCategory(finder.getCategoryName());
        if (finder.getStatus() != null)
            pro3 = (List<Book>) this.finderRepository.getBooksByStatus(finder.getStatus());
        if (!(pro1 == null && pro2 == null && pro3 == null)) {
            pro1.retainAll(pro2);
            pro1.retainAll(pro3);

            result = pro1;
        }
        return result;
    }

    public Collection<Finder> findAllByBook(final int bookId){

        Collection<Finder> result;

        result = this.finderRepository.findAllByBook(bookId);

        return result;
    }
}