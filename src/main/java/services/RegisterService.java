package services;

import domain.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.RegisterRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class RegisterService {
    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    ActorService actorService;

    public Register create(){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("READER"));

        Register res = new Register();

        return res;
    }

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


    //TODO: Save register
}
