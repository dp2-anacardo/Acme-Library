package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.TransactionRepository;

import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private MessageService messageService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private Validator validator;


    public Transaction reconstructSale(Transaction transaction, BindingResult binding){
        Transaction result;
        if(transaction.getId() == 0){
            result = this.create();
            result.setTicker(this.tickerGenerator());
            result.setIsSale(true);
            result.setSeller(this.readerService.findOne(this.actorService.getActorLogged().getId()));
            result.setPrice(transaction.getPrice());
            result.setBook(transaction.getBook());
        }else{
            result = this.findOne(transaction.getId());
            Assert.isTrue(result.getIsSale() == true && result.getIsFinished() == false);
            result.setBuyer(this.readerService.findOne(this.actorService.getActorLogged().getId()));
            result.setMoment(new Date());
            result.setCreditCard(transaction.getCreditCard());
        }

        validator.validate(result,binding);

        if(transaction.getId() != 0){
            final Collection<String> brandNames = this.configurationService.getConfiguration().getBrandName();
            if(!brandNames.contains(result.getCreditCard().getBrandName()))
                binding.rejectValue("creditCard.brandName", "sponsorship.creditCard.brandName.error");

//        (!result.getEvent().getIsFinal())
//            binding.rejectValue("event", "sponsorship.event.error.notFinal");

            final Date date = new Date();
            if(result.getCreditCard().getExpirationYear() != null && result.getCreditCard().getExpirationYear().before(date))
                binding.rejectValue("creditCard.expirationYear", "sponsorship.creditCard.expiration.future");
        }
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
        if(t.getId() == 0){
            t.setSeller(this.readerService.findOne(this.actorService.getActorLogged().getId()));
            t.setTicker(tickerGenerator());
            t.setIsSale(true);
            t.setIsFinished(false);
        } else {
            t.setIsFinished(true);
            final Message message = this.messageService.create();
            final Collection<Actor> recipients = new ArrayList<Actor>();
            recipients.add(t.getSeller());
            message.setRecipients(recipients);
            message.setSubject("Your book has been sold. \n Tu libro ha sido vendido.");
            message.setBody("Your book: "+t.getBook().getTitle()+" has been sold. \n Tu libro: "+t.getBook().getTitle()+
                    " ha sido vendido.");
            this.messageService.notification(message);
        }
        Transaction result = this.transactionRepository.save(t);
        return result;
    }

    public Transaction saveExchange(Transaction t){
        Assert.isTrue(t.getId() == 0);
        Assert.isTrue(t.getBook() != null);
        t.setSeller(this.readerService.findOne(this.actorService.getActorLogged().getId()));
        t.setTicker(tickerGenerator());
        t.setIsSale(false);
        t.setIsFinished(false);
        Transaction result = this.transactionRepository.save(t);
        return result;
    }

    public void delete(Transaction t){
        Assert.isTrue(t.getSeller().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
        Assert.isTrue(t.getIsFinished() == false);
        this.transactionRepository.delete(t);
    }

    public void delete3(Transaction t){
        this.transactionRepository.delete(t);
    }

    public void updateDelete(Transaction t){
        t.setBuyer(null);
        this.transactionRepository.save(t);
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

    public Collection<Transaction> getBuysByReader(){
        Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
        Assert.notNull(r);
        return this.transactionRepository.getBuysByReader(r);
    }

    public Collection<Transaction> getExchangesByReader(){
        Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
        Assert.notNull(r);
        return this.transactionRepository.getExchangesByReader(r);
    }

    public Collection<Transaction> getSalesWithoutBuyer(){
        return this.transactionRepository.getSalesWithoutBuyer();
    }

    public Collection<Transaction> getExchanges(){
        return this.transactionRepository.getExchanges();
    }

    public Collection<Transaction> findAllNotFinished() {return this.transactionRepository.findAllNotFinished();}
    public Transaction getTransactionByComplaint(final int complaintId){
        return this.transactionRepository.getTransactionByComplaint(complaintId);
    }

}
