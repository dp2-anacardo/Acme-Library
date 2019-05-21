package services;

import domain.Offer;
import domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.OfferRepository;

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
    private ReaderService readerService;

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
       // Assert.isTrue(o.getBook().getReader().equals(this.readerService.findOne(this.actorService.getActorLogged().getId())));
        Assert.isTrue(o.getId()==0);
        o.setReader(this.readerService.findOne(this.actorService.getActorLogged().getId()));
        o.setMoment(new Date());
        o.setStatus("PENDING");
        Offer result = this.offerRepository.save(o);
        return result;
    }

    public void delete(Offer o){
        Assert.isTrue(o.getReader().equals(this.actorService.getActorLogged()));
        this.offerRepository.delete(o);
    }
}
