package services;

import domain.Event;
import domain.Reader;
import domain.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.RegisterRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class RegisterService {
    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ReaderService readerService;

    public Collection<Register> findAll(){
        Collection<Register> res;
        res = this.registerRepository.findAll();
        Assert.notNull(res);
        return res;
    }

    public Register findOne(int registerId){
        Register res;
        res = this.registerRepository.findOne(registerId);
        Assert.notNull(res);
        return res;
    }


    public Register save(int eventId){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Event event = this.eventService.findOne(eventId);
        Assert.notNull(event);
        Assert.isTrue(event.getMaximumCapacity() > 0);

        Assert.isTrue(event.getActualCapacity() < event.getMaximumCapacity());

        Register register = new Register();
        register.setMoment(new Date());
        Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
        register.setReader(r);
        Register result = this.registerRepository.save(register);
        Collection<Event> events = this.getEventsPerReader(r);
        Assert.isTrue(events.contains(event) == false);

        Collection<Register> registers = event.getRegisters();
        registers.add(result);
        event.setRegisters(registers);

        return result;
    }

    public void cancel(int registerId, int eventId){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Event event = this.eventService.findOne(eventId);
        Assert.notNull(event);

        Register register = this.registerRepository.findOne(registerId);
        Assert.notNull(register);

        Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
        Collection<Event> events = this.getEventsPerReader(r);
        Assert.isTrue(events.contains(event));


        Date now = new Date();
        Assert.isTrue(now.before(register.getMoment()));

        Collection<Register> registers = event.getRegisters();
        registers.remove(register);

        event.setRegisters(registers);
    }

    public void delete(final int registerId){
        this.registerRepository.delete(registerId);
    }

   public Collection<Event> getEventsPerReader(Reader reader){
        Collection<Event> res;
        res = this.registerRepository.getEventsPerReader(reader);
        Assert.notNull(res);
        return res;
   }

    public Collection<Register> getRegistersPerReader(final int readerId){
        return this.registerRepository.getRegistersPerReader(readerId);
    }


}
