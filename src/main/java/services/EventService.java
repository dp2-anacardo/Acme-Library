package services;

import domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.EventRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class EventService {
    //Manager repository
    @Autowired
    EventRepository eventRepository;

    @Autowired
    ActorService    actorService;

    public Event create(){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER"));

        Event res = new Event();

        return res;
    }

    public Collection<Event> findAll(){
        Collection<Event> res;
        res = this.eventRepository.findAll();
        Assert.notNull(res);
        return res;
    }

    public Event findOne(int eventId){
        Event res;
        res = this.eventRepository.findOne(eventId);
        Assert.notNull(res);
        return res;
    }
}
