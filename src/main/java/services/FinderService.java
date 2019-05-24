package services;

import domain.Book;
import domain.Configuration;
import domain.Finder;
import domain.Transaction;
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
    @Autowired
    private TransactionService transactionService;

    //Validator
    @Autowired
    private Validator validator;


    //Simple CRUD Methods
    public Finder create() {
        Finder result;
        result = new Finder();
        final Collection<Transaction> transactions = new ArrayList<>();
        result.setTransactions(transactions);
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

        Collection<Transaction> result = this.search(finder);

        if (result.isEmpty() && (finder.getKeyWord() == null || finder.getKeyWord().equals(""))
                && (finder.getCategoryName() == null || finder.getCategoryName().equals(""))
                && (finder.getStatus() == null || finder.getCategoryName().equals("")))
            result = this.transactionService.findAllNotFinished();

        Configuration conf;
        conf = this.configurationService.getConfiguration();

        result = this.maxPosition(result, conf);

        finder.setTransactions(result);
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

    public Collection<Transaction> getTransactionsByKeyWord(String keyword) {
        return this.finderRepository.getTransactionsByKeyWord(keyword);
    }

    public Collection<Transaction> getTransactionsContainsKeyWord(String keyword) {
        return this.finderRepository.getTransactionsContainsKeyWord(keyword);
    }

    public Collection<Transaction> maxPosition(Collection<Transaction> result, Configuration configuration) {
        if (result.size() > configuration.getMaxResults()) {

            final List<Transaction> copy = (List<Transaction>) result;

            final List<Transaction> transLim = new ArrayList<>();
            for (int i = 0; i < configuration.getMaxResults(); i++)
                transLim.add(copy.get(i));
            result = transLim;
        }

        return result;
    }

    public Collection<Transaction> search(Finder finder) {
        Collection<Transaction> result = Collections.emptyList();
        List<Transaction> pro1 = null;
        List<Transaction> pro2 = null;
        List<Transaction> pro3 = null;
        List<Transaction> proAux1;
        List<Transaction> proAux2;

        if (!(finder.getKeyWord() == null || finder.getKeyWord().equals(""))) {
            proAux1 = (List<Transaction>) this.finderRepository.getTransactionsByKeyWord(finder.getKeyWord());
            proAux2 = (List<Transaction>) this.finderRepository.getTransactionsContainsKeyWord(finder.getKeyWord());

            Set<Transaction> set = new LinkedHashSet<>(proAux1);
            set.addAll(proAux2);
            pro1 = new ArrayList<>(set);
        }
        if (!(finder.getCategoryName() == null || finder.getCategoryName().equals("")))
            pro2 = (List<Transaction>) this.finderRepository.getTransactionsByCategory(finder.getCategoryName());
        if (!(finder.getStatus() == null || finder.getStatus().equals("")))
            pro3 = (List<Transaction>) this.finderRepository.getTransactionsByStatus(finder.getStatus());

        if (!(pro1 == null && pro2 == null && pro3 == null)) {
            if (pro1 == null)
                pro1 = (List<Transaction>) this.transactionService.findAllNotFinished();
            if (pro2 == null)
                pro2 = (List<Transaction>) this.transactionService.findAllNotFinished();
            if (pro3 == null)
                pro3 = (List<Transaction>) this.transactionService.findAllNotFinished();
            pro1.retainAll(pro2);
            pro1.retainAll(pro3);

            result = pro1;
        }

        return result;
    }

    public Collection<Finder> findAllByTransaction(final int transactionId){

        Collection<Finder> result;

        result = this.finderRepository.findAllByTransaction(transactionId);

        return result;
    }
}