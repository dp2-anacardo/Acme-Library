
package services;

import domain.*;
import forms.OrganizerForm;
import forms.RefereeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.OrganizerRepository;
import repositories.RefereeRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class OrganizerService {

    //Managed Repositories
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private MessageBoxService messageBoxService;
    //Supporting services
    @Autowired
    private ActorService actorService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private Validator validator;


    public Organizer create() {

        final Authority auth;
        final UserAccount userAccount;
        final Collection<Authority> authorities;
        final Collection<SocialProfile> profiles;
        final Collection<MessageBox> boxes;
        final Organizer a = new Organizer();
        userAccount = new UserAccount();
        auth = new Authority();
        authorities = new ArrayList<Authority>();
        profiles = new ArrayList<SocialProfile>();
        boxes = new ArrayList<MessageBox>();


        auth.setAuthority(Authority.ORGANIZER);
        authorities.add(auth);
        userAccount.setAuthorities(authorities);
        a.setUserAccount(userAccount);
        a.setIsBanned(false);
        a.setIsSuspicious(false);
        a.setSocialProfiles(profiles);
        a.setBoxes(boxes);
        return a;
    }

    public Collection<Organizer> findAll() {

        Collection<Organizer> result;
        result = this.organizerRepository.findAll();
        return result;
    }

    public Organizer findOne(final int organizerId) {

        Assert.isTrue(organizerId != 0);
        Organizer result;
        result = this.organizerRepository.findOne(organizerId);
        return result;
    }

    public Organizer save(final Organizer o) {
        Assert.notNull(o);
        Organizer result;
        final char[] c = o.getPhoneNumber().toCharArray();
        if ((!o.getPhoneNumber().equals(null) && !o.getPhoneNumber().equals("")))
            if (c[0] != '+') {
                final String i = this.configurationService.findAll().get(0).getCountryCode();
                o.setPhoneNumber("+" + i + " " + o.getPhoneNumber());
            }
        if (o.getId() == 0) {
            final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            final String res = encoder.encodePassword(o.getUserAccount().getPassword(), null);
            o.getUserAccount().setPassword(res);
            o.setBoxes(this.messageBoxService.createSystemMessageBox());
        }
        result = this.organizerRepository.save(o);
        return result;
    }

    public void delete(final Organizer o) {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ORGANIZER"));
        Assert.notNull(o);
        Assert.isTrue(actor.getId() != 0);

        this.organizerRepository.delete(o);
    }

    public Organizer reconstruct(final Organizer o, final BindingResult binding) {

        Organizer result;
        if (o.getId() == 0) {
            this.validator.validate(o, binding);
            result = o;
        } else {
            result = this.organizerRepository.findOne(o.getId());

            result.setName(o.getName());
            result.setPhoto(o.getPhoto());
            result.setPhoneNumber(o.getPhoneNumber());
            result.setEmail(o.getEmail());
            result.setAddress(o.getAddress());
            result.setSurname(o.getSurname());

            this.validator.validate(o, binding);
        }
        return result;
    }

    //Objeto formulario
    public Organizer reconstruct(final OrganizerForm o, final BindingResult binding) {

        final Organizer result = this.create();
        result.setAddress(o.getAddress());
        result.setEmail(o.getEmail());
        result.setId(o.getId());
        result.setName(o.getName());
        result.setPhoneNumber(o.getPhoneNumber());
        result.setPhoto(o.getPhoto());
        result.setSurname(o.getSurname());
        result.getUserAccount().setPassword(o.getPassword());
        result.getUserAccount().setUsername(o.getUsername());
        result.setVersion(o.getVersion());
        result.setMiddleName(o.getMiddleName());

        this.validator.validate(result, binding);
        return result;
    }

}
