package services;

import domain.Offer;
import domain.Reader;
import domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.TransactionRepository;

import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

@Service
@Transactional
public class TransactionService {

    //Managed repository
    @Autowired
    private TransactionRepository transactionRepository;
    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private Validator validator;


    public Transaction reconstructSale(Transaction transaction, BindingResult binding){
        Transaction result;
        result = this.create();

        result.setTicker(this.tickerGenerator());
        result.setIsSale(true);
        result.setSeller(this.readerService.findOne(this.actorService.getActorLogged().getId()));
        result.setMoment(new Date());
        result.setPrice(transaction.getPrice());
        result.setBook(transaction.getBook());
        validator.validate(result,binding);
        if(binding.hasErrors()){
            throw new ValidationException();
        }
        return result;
    }

    public Transaction create(){
        Assert.isTrue(this.actorService.getActorLogged() instanceof Reader);
        Transaction transaction = new Transaction();
        return transaction;
    }

    public Transaction findOne(int id){
        return this.transactionRepository.findOne(id);
    }

    public Collection<Transaction> findAll(){
        return this.transactionRepository.findAll();
    }

    public Transaction saveSale(Transaction t){
        Assert.isTrue(t.getId() == 0);
        t.setSeller(this.readerService.findOne(this.actorService.getActorLogged().getId()));
        t.setTicker(tickerGenerator());
        t.setIsSale(true);
        Transaction result = this.transactionRepository.save(t);
        return result;
    }

    public Transaction saveExchange(Transaction t){
        Assert.isTrue(t.getId() == 0);
        t.setSeller(this.readerService.findOne(this.actorService.getActorLogged().getId()));
        t.setTicker(tickerGenerator());
        t.setIsSale(false);
        Transaction result = this.transactionRepository.save(t);
        return result;
    }

    public void delete(Transaction t){
        Assert.isTrue(t.getSeller().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
        Assert.isTrue(t.getIsFinished() == false);
        this.transactionRepository.delete(t);
    }

    //ESTO PARA EL DELETE DE ACTOR
    public void delete2(Transaction t){
        Assert.isTrue(t.getBuyer().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
        if(t.getIsSale()){
            this.transactionRepository.delete(t);
        }else{
            for(Offer o : t.getOffers()){
                this.offerService.delete(o);
            }
        }
        this.transactionRepository.delete(t);

    }

    private String tickerGenerator() {
        String dateRes = "";
        String numericRes = "";
        final String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        dateRes = new SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime());

        for (int i = 0; i < 5; i++) {
            final Random random = new Random();
            numericRes = numericRes + alphanumeric.charAt(random.nextInt(alphanumeric.length() - 1));
        }

        return dateRes + "-" + numericRes;
    }

    public Collection<Transaction> getSalesByReader(){
        Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
        Assert.notNull(r);
        return this.transactionRepository.getSalesByReader(r);
    }

    public Collection<Transaction> getExchangesByReader(){
        Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
        Assert.notNull(r);
        return this.transactionRepository.getExchangesByReader(r);
    }
}
