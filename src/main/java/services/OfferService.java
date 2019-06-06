package services;

import domain.Actor;
import domain.Message;
import domain.Offer;
import domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.OfferRepository;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class OfferService {

    //Managed Repository
    @Autowired
    private OfferRepository offerRepository;
    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    Validator validator;

    public Offer reconstruct(Offer offer, BindingResult binding){
        Offer result = this.create();
        result.setReader(this.readerService.findOne(this.actorService.getActorLogged().getId()));
        result.setMoment(new Date());
        result.setStatus("PENDING");
        result.setComment(offer.getComment());
        result.setBook(offer.getBook());
        result.setTransaction(offer.getTransaction());

        validator.validate(result,binding);
        if(binding.hasErrors())
            throw new ValidationException();
        return result;
    }


    public Offer create(){
        Assert.isTrue(this.actorService.getActorLogged() instanceof Reader);
        Offer result = new Offer();
        return result;
    }

    public Offer findOne(int id){
        return this.offerRepository.findOne(id);
    }

    public Collection<Offer> findAll(){
        return this.offerRepository.findAll();
    }

    public Offer save(Offer o){
        Assert.isTrue(o.getBook().getReader().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
        Assert.isTrue(o.getTransaction().getIsSale() == false);
        Assert.isTrue(o.getId()==0);
        for(Offer of : o.getTransaction().getOffers()){
            if(of.getReader() == this.readerService.findOne(this.actorService.getActorLogged().getId()) || of.getStatus().equals("ACCEPTED") ){
                Assert.isTrue(false);
            }
        }
        Offer result = this.offerRepository.save(o);
        return result;
    }

    public Offer accept(Offer o){
        Assert.isTrue(o.getTransaction().getSeller().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
        Assert.isTrue(o.getTransaction().getIsSale() == false);
        Assert.isTrue(o.getStatus().equals("PENDING"));
        for(Offer of : o.getTransaction().getOffers()){
            if(of.getReader() == this.readerService.findOne(this.actorService.getActorLogged().getId()) || of.getStatus().equals("ACCEPTED") ){
                Assert.isTrue(false);
            }
            if(of != o)
                of.setStatus("REJECTED");
        }
        o.getTransaction().setIsFinished(true);
        o.setStatus("ACCEPTED");
        Offer result = this.offerRepository.save(o);

        final Message message = this.messageService.create();
        final Collection<Actor> recipients = new ArrayList<Actor>();
        recipients.add(o.getReader());
        message.setRecipients(recipients);
        message.setSubject("Your offer has been accepted. \n Tu libro oferta ha sido aceptada.");
        message.setBody("Your offer for the book: "+o.getTransaction().getBook().getTitle()+" has been accepted. \n Tu oferta por el libro: "+o.getTransaction().getBook().getTitle()+
                " ha sido aceptada.");
        this.messageService.notification(message);
        return result;
    }

    public Offer reject(Offer o){
        Assert.isTrue(o.getTransaction().getSeller().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
        Assert.isTrue(o.getStatus().equals("PENDING"));
        o.setStatus("REJECTED");
        Offer result = this.offerRepository.save(o);
        return result;
    }

    public void delete(Offer o){
        Assert.isTrue(o.getReader().equals(this.actorService.getActorLogged()));
        o.getTransaction().getOffers().remove(o);
        this.offerRepository.delete(o);
    }

    public void deleteForce(Offer o){
        this.offerRepository.delete(o);
    }

    public Collection<Offer> getOffersByReader(){
        Reader r = this.readerService.findOne(actorService.getActorLogged().getId());
        return this.offerRepository.getOffersByReader(r);
    }

    public Collection<Offer> getAllByReader(final int readerId){
        return this.offerRepository.getAllByReader(readerId);
    }
}
