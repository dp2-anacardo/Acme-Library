
package services;

import domain.*;
import forms.AdministratorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class AdministratorService {

    //Managed Repositories
    @Autowired
    private AdministratorRepository administratorRepository;

    //Supporting services
    @Autowired
    private ActorService actorService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private MessageBoxService messageBoxService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SponsorService sponsorService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private OrganizerService organizerService;
    @Autowired
    private SponsorshipService sponsorshipService;
    @Autowired
    private BookService bookService;
    @Autowired
    private Validator validator;


    public Administrator create() {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        final Authority auth;
        final UserAccount userAccount;
        final Collection<Authority> authorities;
        final Collection<SocialProfile> profiles;
        final Collection<MessageBox> boxes;
        final Administrator a = new Administrator();
        userAccount = new UserAccount();
        auth = new Authority();
        authorities = new ArrayList<Authority>();
        profiles = new ArrayList<SocialProfile>();
        boxes = new ArrayList<MessageBox>();


        auth.setAuthority(Authority.ADMIN);
        authorities.add(auth);
        userAccount.setAuthorities(authorities);
        a.setUserAccount(userAccount);
        a.setIsBanned(false);
        a.setSocialProfiles(profiles);
        a.setBoxes(boxes);
        return a;
    }

    public Collection<Administrator> findAll() {
        Collection<Administrator> result;

        result = this.administratorRepository.findAll();

        return result;
    }

    public Administrator findOne(final int administratorId) {
        Assert.isTrue(administratorId != 0);

        Administrator result;

        result = this.administratorRepository.findOne(administratorId);

        return result;
    }

    public Administrator save(final Administrator administrator) {
        UserAccount userAccount;
        userAccount = LoginService.getPrincipal();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        Assert.notNull(administrator);
        Administrator result;
        final char[] c = administrator.getPhoneNumber().toCharArray();
        if ((!administrator.getPhoneNumber().equals(null) && !administrator.getPhoneNumber().equals("")))
            if (c[0] != '+') {
                final String i = this.configurationService.findAll().get(0).getCountryCode();
                administrator.setPhoneNumber("+" + i + " " + administrator.getPhoneNumber());
            }
        if (administrator.getId() == 0) {
            final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            final String res = encoder.encodePassword(administrator.getUserAccount().getPassword(), null);
            administrator.getUserAccount().setPassword(res);
            administrator.setBoxes(this.messageBoxService.createSystemMessageBox());
        }
        result = this.administratorRepository.save(administrator);
        return result;
    }

    public void delete(final Administrator administrator) {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        Assert.notNull(administrator);
        Assert.isTrue(actor.getId() != 0);

        this.administratorRepository.delete(administrator);
    }

    public Administrator reconstruct(final Administrator admin, final BindingResult binding) {

        Administrator result;
        if (admin.getId() == 0) {
            this.validator.validate(admin, binding);
            result = admin;
        } else {
            result = this.administratorRepository.findOne(admin.getId());

            result.setName(admin.getName());
            result.setPhoto(admin.getPhoto());
            result.setPhoneNumber(admin.getPhoneNumber());
            result.setEmail(admin.getEmail());
            result.setAddress(admin.getAddress());
            result.setSurname(admin.getSurname());

            this.validator.validate(admin, binding);
        }
        return result;
    }

    //Validador de contraseñas
    public Boolean checkPass(final String pass, final String confirmPass) {
        Boolean res = false;
        if (pass.compareTo(confirmPass) == 0)
            res = true;
        return res;
    }

    //Objeto formulario
    public Administrator reconstruct(final AdministratorForm admin, final BindingResult binding) {

        final Administrator result = this.create();
        result.setAddress(admin.getAddress());
        result.setEmail(admin.getEmail());
        result.setId(admin.getId());
        result.setName(admin.getName());
        result.setPhoneNumber(admin.getPhoneNumber());
        result.setPhoto(admin.getPhoto());
        result.setSurname(admin.getSurname());
        result.getUserAccount().setPassword(admin.getPassword());
        result.getUserAccount().setUsername(admin.getUsername());
        result.setVersion(admin.getVersion());

        this.validator.validate(result, binding);
        return result;
    }

    public void ban(final Actor actor) {
        final Actor principal = this.actorService.getActorLogged();
        Assert.isTrue(principal instanceof Administrator);
        Assert.isTrue(!actor.getIsBanned());
        Assert.isTrue(actor.getIsSuspicious());

        actor.setIsBanned(true);

        this.actorService.save(actor);
    }

    public void unban(final Actor actor) {
        final Actor principal = this.actorService.getActorLogged();
        Assert.isTrue(principal instanceof Administrator);
        Assert.isTrue(actor.getIsBanned());

        actor.setIsBanned(false);

        this.actorService.save(actor);
    }


    public void computeAllScores() {

        Collection<Reader> readers;
        Collection<Organizer> organizers;
        Collection<Sponsor> sponsors;

        // Make sure that the principal is an Admin
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Administrator.class, principal);

        readers = this.readerService.findAll();
        for (final Reader reader : readers) {
            reader.setScore(this.computeScore(this.messageService.findAllSentByActor(reader.getId())));
            this.readerService.save(reader);
        }

        organizers = this.organizerService.findAll();
        for (final Organizer organizer : organizers) {
            organizer.setScore(this.computeScore(this.messageService.findAllSentByActor(organizer.getId())));
            this.organizerService.save(organizer);
        }

        sponsors = this.sponsorService.findAll();
        for (final Sponsor sponsor : sponsors) {
            sponsor.setScore(this.computeScore(this.messageService.findAllSentByActor(sponsor.getId())));
            this.sponsorService.save(sponsor);
        }
    }

    private Double computeScore(final Collection<Message> messages) {
        final Collection<String> positiveWords = this.configurationService.getConfiguration().getPosWords();

        final Collection<String> negativeWords = this.configurationService.getConfiguration().getNegWords();

        Double positiveWordsValue = 0.0;
        Double negativeWordsValue = 0.0;

        for (final Message message : messages) {
            final String body = message.getBody();
            final String subject = message.getSubject();

            for (final String positiveWord : positiveWords) {
                System.out.println(positiveWord);
                if (body.contains(positiveWord)) {
                    positiveWordsValue += 1.0;
                    System.out.println(positiveWordsValue);
                }
            }
            for (final String negativeWord : negativeWords) {
                System.out.println(negativeWord);
                if (body.contains(negativeWord)) {
                    negativeWordsValue += 1.0;
                    System.out.println(negativeWordsValue);
                }
            }

            for (final String positiveWord : positiveWords) {
                System.out.println(positiveWord);
                if (subject.contains(positiveWord)) {
                    positiveWordsValue += 1.0;
                    System.out.println(positiveWordsValue);
                }
            }
            for (final String negativeWord : negativeWords) {
                System.out.println(negativeWord);
                if (subject.contains(negativeWord)) {
                    negativeWordsValue += 1.0;
                    System.out.println(negativeWordsValue);
                }
            }
        }

        // check for NaN values
        if (positiveWordsValue + negativeWordsValue == 0)
            return 0.0;
        else if (positiveWordsValue - negativeWordsValue == 0)
            return 0.0;
        else
            return (positiveWordsValue - negativeWordsValue) / (positiveWordsValue + negativeWordsValue);

    }

    public void computeAllSpam() {

        Collection<Reader> readers;
        Collection<Organizer> organizers;
        Collection<Sponsor> sponsors;

        // Make sure that the principal is an Admin
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Administrator.class, principal);

        readers = this.readerService.findAll();
        for (final Reader reader : readers) {
            reader.setIsSuspicious(this.messageService.findSpamRatioByActor(reader.getId()) > .10);
            this.readerService.save(reader);
        }

        organizers = this.organizerService.findAll();
        for (final Organizer organizer : organizers) {
            organizer.setIsSuspicious(this.messageService.findSpamRatioByActor(organizer.getId()) > .10);
            this.organizerService.save(organizer);
        }

        sponsors = this.sponsorService.findAll();
        for (final Sponsor sponsor : sponsors) {
            sponsor.setIsSuspicious(this.messageService.findSpamRatioByActor(sponsor.getId()) > .10);
            this.sponsorService.save(sponsor);
        }
    }

    public void desactivateExpiredSponsorships() {
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Administrator.class, principal);

        final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllExpiredCreditCard();

        for (final Sponsorship s : sponsorships)
            this.sponsorshipService.desactivate(s);

    }

    public void deleteInactiveBooks() {
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Administrator.class, principal);

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        java.sql.Date oneMonthOld = new java.sql.Date(cal.getTimeInMillis());

        final Collection<Book> books = this.bookService.findAllInactiveBooks(oneMonthOld);

        for (final Book b : books)
            this.bookService.deleteAdmin(b);

    }

    //DASHBOARD
    //Q8
    public Double getRatioOfFullFinders() {
        return this.administratorRepository.getRatioOfFullFinders();
    }

    //Q9
    public Double getRatioOfEmptyFinders() {
        return this.administratorRepository.getRatioOfEmptyFinders();
    }

    //Q10
    public Double getRatioOfEmptyVSFullFinders() {
        return this.administratorRepository.getRatioOfEmptyVSFullFinders();
    }

    //Q13
    public Double getRatioOfSalesVSExchangesByReader() {
        return this.administratorRepository.getRatioOfSalesVSExchangesByReader();
    }

}
