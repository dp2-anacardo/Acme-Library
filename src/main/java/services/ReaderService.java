
package services;

import domain.*;
import forms.OrganizerForm;
import forms.ReaderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.OrganizerRepository;
import repositories.ReaderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class ReaderService {

    //Managed Repositories
    @Autowired
    private ReaderRepository readerRepository;
    //Supporting services
    @Autowired
    private MessageBoxService messageBoxService;

    @Autowired
    private FinderService finderService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private Validator validator;


    public Reader create() {

        final Authority auth;
        final UserAccount userAccount;
        final Collection<Authority> authorities;
        final Collection<SocialProfile> profiles;
        final Collection<MessageBox> boxes;
        final Reader a = new Reader();
        userAccount = new UserAccount();
        auth = new Authority();
        authorities = new ArrayList<Authority>();
        profiles = new ArrayList<SocialProfile>();
        boxes = new ArrayList<MessageBox>();


        auth.setAuthority(Authority.READER);
        authorities.add(auth);
        userAccount.setAuthorities(authorities);
        a.setUserAccount(userAccount);
        a.setIsBanned(false);
        a.setIsSuspicious(false);
        a.setSocialProfiles(profiles);
        a.setBoxes(boxes);

        return a;
    }

    public Collection<Reader> findAll() {

        Collection<Reader> result;
        result = this.readerRepository.findAll();
        return result;
    }

    public Reader findOne(final int readerId) {

        Assert.isTrue(readerId != 0);
        Reader result;
        result = this.readerRepository.findOne(readerId);
        return result;
    }

    public Reader save(final Reader r) {

        Assert.notNull(r);
        Reader result;
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
            Finder definitivo = this.finderService.save(this.finderService.create());
            r.setFinder(definitivo);

        }
        result = this.readerRepository.save(r);
        return result;
    }

    public void updateAdmin(final Reader r){
        this.readerRepository.save(r);
    }

    public void delete(final Reader r) {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("READER"));
        Assert.notNull(r);
        Assert.isTrue(actor.getId() != 0);

        this.readerRepository.delete(r);
    }

    public Reader reconstruct(final Reader r, final BindingResult binding) {

        Reader result;
        if (r.getId() == 0) {
            this.validator.validate(r, binding);
            result = r;
        } else {
            result = this.readerRepository.findOne(r.getId());

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
    public Reader reconstruct(final ReaderForm r, final BindingResult binding) {

        final Reader result = this.create();
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
        result.setMiddleName(r.getMiddleName());

        this.validator.validate(result, binding);
        return result;
    }

    public void flush(){
        this.readerRepository.flush();
    }

}
