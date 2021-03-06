package services;

import domain.Event;
import domain.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.EventRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;

@Service
@Transactional
public class EventService {
    //Manager repository
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ActorService    actorService;

    @Autowired
    private OrganizerService    organizerService;

    @Autowired
    private Validator validator;

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


    public Event save(Event e){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER"));

        Assert.notNull(e);
        Event res;
        if (e.getId() == 0){
            int id = this.actorService.getActorLogged().getId();
            Organizer o = this.organizerService.findOne(id);
            e.setOrganizer(o);
        }
        Assert.isTrue(e.getOrganizer().getId() == this.actorService.getActorLogged().getId());
        res = this.eventRepository.save(e);
        return res;
    }


    public Event saveDraft(Event e){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER"));

        Assert.notNull(e);

        Event res;

        e.setIsFinal(false);
        res = this.save(e);
        return res;

    }

    public Event saveFinal(Event e){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER"));

        Assert.notNull(e);
        Event res;

        e.setIsFinal(true);
        Assert.isTrue(e.getMaximumCapacity() > 0);
        res = this.save(e);
        return res;
    }

    public void delete(Event e){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER"));

        Assert.notNull(e);
        Assert.isTrue(e.getId() != 0);
        Assert.isTrue(e.getIsFinal() == false);
        Assert.isTrue(e.getOrganizer().getId() == this.actorService.getActorLogged().getId());

        this.eventRepository.delete(e);
    }

    public Event reconstruct(Event event, BindingResult binding){
        Event result;
        if (event.getId() == 0){
            result = this.create();
        } else{
            result = this.eventRepository.findOne(event.getId());
        }

        result.setTitle(event.getTitle());
        result.setDescription(event.getDescription());
        result.setDate(event.getDate());
        result.setAddress(event.getAddress());
        result.setMaximumCapacity(event.getMaximumCapacity());
        result.setRegisters(event.getRegisters());

        validator.validate(result, binding);

        if (binding.hasErrors()){
            throw new ValidationException();
        }
        return result;
    }

    public Collection<Event> findAllInFinal(){

        Collection<Event> result;

        result = this.eventRepository.findAllInFinal();

        return result;
    }

    public Collection<Event> getFutureEventsFinal(){
        return this.eventRepository.getFutureEventsFinal();
    }

    public Collection<Event> getEventsPerOrOrganizer(int organizerId) {
        Collection<Event> res;
        res = this.eventRepository.getEventsPerOrOrganizer(organizerId);
        Assert.notNull(res);
        return res;
    }
    public void deleteForced(Event e){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER"));

        Assert.notNull(e);
        Assert.isTrue(e.getId() != 0);

        this.eventRepository.delete(e);
    }

    public Event findByRegister(final int registerId){
        return this.eventRepository.findByRegister(registerId);
    }
}
