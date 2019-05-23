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


    public void save(int eventId){
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

        Collection<Register> registers = event.getRegisters();
        registers.add(register);
        event.setRegisters(registers);
    }

    public void delete(int registerId, int eventId){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Event event = this.eventService.findOne(eventId);
        Assert.notNull(event);

        Register register = this.registerRepository.findOne(registerId);
        Assert.notNull(register);

        Date now = new Date();
        Assert.isTrue(now.before(register.getMoment()));

        Collection<Register> registers = event.getRegisters();
        registers.remove(register);

        event.setRegisters(registers);
    }
}
