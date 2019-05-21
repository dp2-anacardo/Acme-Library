package services;

import domain.Offer;
import domain.Reader;
import domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.TransactionRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
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
        Assert.isTrue(t.getBuyer().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
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
