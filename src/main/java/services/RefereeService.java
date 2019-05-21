
package services;

import domain.Actor;
import domain.MessageBox;
import domain.Referee;
import domain.SocialProfile;
import forms.RefereeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.RefereeRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class RefereeService {

    //Managed Repositories
    @Autowired
    private RefereeRepository refereeRepository;
    //Supporting services
    @Autowired
    private ActorService actorService;
    @Autowired
    private MessageBoxService messageBoxService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private Validator validator;


    public Referee create() {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        final Authority auth;
        final UserAccount userAccount;
        final Collection<Authority> authorities;
        final Collection<SocialProfile> profiles;
        final Collection<MessageBox> boxes;
        final Referee a = new Referee();
        userAccount = new UserAccount();
        auth = new Authority();
        authorities = new ArrayList<Authority>();
        profiles = new ArrayList<SocialProfile>();
        boxes = new ArrayList<MessageBox>();


        auth.setAuthority(Authority.REFEREE);
        authorities.add(auth);
        userAccount.setAuthorities(authorities);
        a.setUserAccount(userAccount);
        a.setIsBanned(false);
        a.setIsSuspicious(false);
        a.setSocialProfiles(profiles);
        a.setBoxes(boxes);
        return a;
    }

    public Collection<Referee> findAll() {

        Collection<Referee> result;
        result = this.refereeRepository.findAll();
        return result;
    }

    public Referee findOne(final int refereeId) {

        Assert.isTrue(refereeId != 0);
        Referee result;
        result = this.refereeRepository.findOne(refereeId);
        return result;
    }

    public Referee save(final Referee r) {
        UserAccount userAccount;
        userAccount = LoginService.getPrincipal();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("REFEREE"));
        Assert.notNull(r);
        Referee result;
        final char[] c = r.getPhoneNumber().toCharArray();
        if ((!r.getPhoneNumber().equals(null) && !r.getPhoneNumber().equals("")))
            if (c[0] != '+') {
                final String i = this.configurationService.findAll().get(0).getCountryCode();
                r.setPhoneNumber("+" + i + " " + r.getPhoneNumber());
            }
        if (r.getId() == 0) {
            final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            final String res = encoder.encodePassword(r.getUserAccount().getPassword(), null);
            r.getUserAccount().setPassword(res);
            r.setBoxes(this.messageBoxService.createSystemMessageBox());
        }
        result = this.refereeRepository.save(r);
        return result;
    }

    public void delete(final Referee r) {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("REFEREE"));
        Assert.notNull(r);
        Assert.isTrue(actor.getId() != 0);

        this.refereeRepository.delete(r);
    }

    public Referee reconstruct(final Referee r, final BindingResult binding) {

        Referee result;
        if (r.getId() == 0) {
            this.validator.validate(r, binding);
            result = r;
        } else {
            result = this.refereeRepository.findOne(r.getId());

            result.setName(r.getName());
            result.setPhoto(r.getPhoto());
            result.setPhoneNumber(r.getPhoneNumber());
            result.setEmail(r.getEmail());
            result.setAddress(r.getAddress());
            result.setSurname(r.getSurname());

            this.validator.validate(r, binding);
        }
        return result;
    }

    //Objeto formulario
    public Referee reconstruct(final RefereeForm r, final BindingResult binding) {

        final Referee result = this.create();
        result.setAddress(r.getAddress());
        result.setEmail(r.getEmail());
        result.setId(r.getId());
        result.setName(r.getName());
        result.setPhoneNumber(r.getPhoneNumber());
        result.setPhoto(r.getPhoto());
        result.setSurname(r.getSurname());
        result.getUserAccount().setPassword(r.getPassword());
        result.getUserAccount().setUsername(r.getUsername());
        result.setVersion(r.getVersion());

        this.validator.validate(result, binding);
        return result;
    }

}
