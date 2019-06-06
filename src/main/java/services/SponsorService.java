
package services;

import domain.Actor;
import domain.MessageBox;
import domain.SocialProfile;
import domain.Sponsor;
import forms.SponsorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.SponsorRepository;
import security.Authority;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class SponsorService {

    //Managed Repositories
    @Autowired
    private SponsorRepository sponsorRepository;
    @Autowired
    private MessageBoxService messageBoxService;
    //Supporting services
    @Autowired
    private ActorService actorService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private Validator validator;


    public Sponsor create() {

        final Authority auth;
        final UserAccount userAccount;
        final Collection<Authority> authorities;
        final Collection<SocialProfile> profiles;
        final Collection<MessageBox> boxes;
        final Sponsor a = new Sponsor();
        userAccount = new UserAccount();
        auth = new Authority();
        authorities = new ArrayList<Authority>();
        profiles = new ArrayList<SocialProfile>();
        boxes = new ArrayList<MessageBox>();


        auth.setAuthority(Authority.SPONSOR);
        authorities.add(auth);
        userAccount.setAuthorities(authorities);
        a.setUserAccount(userAccount);
        a.setIsBanned(false);
        a.setIsSuspicious(false);
        a.setSocialProfiles(profiles);
        a.setBoxes(boxes);
        return a;
    }

    public Collection<Sponsor> findAll() {

        Collection<Sponsor> result;
        result = this.sponsorRepository.findAll();
        return result;
    }

    public Sponsor findOne(final int sponsorId) {

        Assert.isTrue(sponsorId != 0);
        Sponsor result;
        result = this.sponsorRepository.findOne(sponsorId);
        return result;
    }

    public Sponsor save(final Sponsor s) {

        Assert.notNull(s);
        Sponsor result;
        final char[] c = s.getPhoneNumber().toCharArray();
        if ((!s.getPhoneNumber().equals(null) && !s.getPhoneNumber().equals("")))
            if (c[0] != '+') {
                final String i = this.configurationService.findAll().get(0).getCountryCode();
                s.setPhoneNumber("+" + i + " " + s.getPhoneNumber());
            }
        if (s.getId() == 0) {
            final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            final String res = encoder.encodePassword(s.getUserAccount().getPassword(), null);
            s.getUserAccount().setPassword(res);
            s.setBoxes(this.messageBoxService.createSystemMessageBox());
        }
        result = this.sponsorRepository.save(s);
        return result;
    }

    public void updateAdmin(final Sponsor r){
        this.sponsorRepository.save(r);
    }

    public void delete(final Sponsor s) {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("SPONSOR"));
        Assert.notNull(s);
        Assert.isTrue(actor.getId() != 0);

        this.sponsorRepository.delete(s);
    }

    public Sponsor reconstruct(final Sponsor s, final BindingResult binding) {

        Sponsor result;
        if (s.getId() == 0) {
            this.validator.validate(s, binding);
            result = s;
        } else {
            result = this.sponsorRepository.findOne(s.getId());

            result.setName(s.getName());
            result.setPhoto(s.getPhoto());
            result.setPhoneNumber(s.getPhoneNumber());
            result.setEmail(s.getEmail());
            result.setAddress(s.getAddress());
            result.setSurname(s.getSurname());

            this.validator.validate(s, binding);
        }
        return result;
    }

    //Objeto formulario
    public Sponsor reconstruct(final SponsorForm s, final BindingResult binding) {

        final Sponsor result = this.create();
        result.setAddress(s.getAddress());
        result.setEmail(s.getEmail());
        result.setId(s.getId());
        result.setName(s.getName());
        result.setPhoneNumber(s.getPhoneNumber());
        result.setPhoto(s.getPhoto());
        result.setSurname(s.getSurname());
        result.getUserAccount().setPassword(s.getPassword());
        result.getUserAccount().setUsername(s.getUsername());
        result.setVersion(s.getVersion());
        result.setMiddleName(s.getMiddleName());

        this.validator.validate(result, binding);
        return result;
    }
    public void flush (){
        this.sponsorRepository.flush();
    }

}
