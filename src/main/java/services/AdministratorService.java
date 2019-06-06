
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

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
    private CategoryService categoryService;
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
        a.setIsSuspicious(false);
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
            result.setMiddleName(admin.getMiddleName());

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
        result.setMiddleName(admin.getMiddleName());
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
        Assert.isTrue(actor.getIsSuspicious() || actor.getScore() < -0.5);

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
            this.readerService.updateAdmin(reader);
        }

        organizers = this.organizerService.findAll();
        for (final Organizer organizer : organizers) {
            organizer.setScore(this.computeScore(this.messageService.findAllSentByActor(organizer.getId())));
            this.organizerService.updateAdmin(organizer);
        }

        sponsors = this.sponsorService.findAll();
        for (final Sponsor sponsor : sponsors) {
            sponsor.setScore(this.computeScore(this.messageService.findAllSentByActor(sponsor.getId())));
            this.sponsorService.updateAdmin(sponsor);
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
                if (body.contains(positiveWord)) {
                    positiveWordsValue += 1.0;
                }
            }
            for (final String negativeWord : negativeWords) {
                if (body.contains(negativeWord)) {
                    negativeWordsValue += 1.0;
                }
            }

            for (final String positiveWord : positiveWords) {
                if (subject.contains(positiveWord)) {
                    positiveWordsValue += 1.0;
                }
            }
            for (final String negativeWord : negativeWords) {
                if (subject.contains(negativeWord)) {
                    negativeWordsValue += 1.0;
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
            this.readerService.updateAdmin(reader);
        }

        organizers = this.organizerService.findAll();
        for (final Organizer organizer : organizers) {
            organizer.setIsSuspicious(this.messageService.findSpamRatioByActor(organizer.getId()) > .10);
            this.organizerService.updateAdmin(organizer);
        }

        sponsors = this.sponsorService.findAll();
        for (final Sponsor sponsor : sponsors) {
            sponsor.setIsSuspicious(this.messageService.findSpamRatioByActor(sponsor.getId()) > .10);
            this.sponsorService.updateAdmin(sponsor);
        }
    }

    public Integer desactivateExpiredSponsorships() {
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Administrator.class, principal);

        final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllExpiredCreditCard();

        for (final Sponsorship s : sponsorships)
            this.sponsorshipService.desactivate(s);

        return sponsorships.size();
    }

    public Integer deleteInactiveBooks() {
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Administrator.class, principal);

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        java.sql.Date oneMonthOld = new java.sql.Date(cal.getTimeInMillis());

        final Collection<Book> books = this.bookService.findAllInactiveBooks(oneMonthOld);

        for (final Book b : books)
            this.bookService.deleteAdmin(b);

        return books.size();
    }

    //DASHBOARD
    //Q2
    public Collection<Organizer> getTop5OrganizersWithMoreEvents(){
        List<Organizer> o = (List<Organizer>) this.administratorRepository.getOrganizersWithMoreEvents();
        Collection<Organizer> result = new ArrayList<>();
        if(o.size()>5){
            for(int i=0; i<5; i++){
                result.add(o.get(i));
            }
        } else result = o;
        return result;
    }

    //Q3
    public List<Double> getTransactionsPrice(){
        List<Double> result = new ArrayList<>();
        result.add(this.administratorRepository.getAvgTransactionsPrice());
        result.add(this.administratorRepository.getMinTransactionsPrice());
        result.add(this.administratorRepository.getMaxTransactionsPrice());
        result.add(this.administratorRepository.getStddevTransactionsPrice());
        return result;
    }

    //Q4
    public List<Double> getBooksPerReader(){
        List<Double> result = new ArrayList<>();
        result.add(this.administratorRepository.getAvgBooksPerReader());
        result.add(this.administratorRepository.getMinBooksPerReader());
        result.add(this.administratorRepository.getMaxBooksPerReader());
        result.add(this.administratorRepository.getStddevBooksPerReader());
        return result;
    }

    //Q5
    public List<Double> getTransactionsComplaint(){
        List<Double> result = new ArrayList<>();
        result.add(this.administratorRepository.getAvgNumberTransactionsComplaints());
        result.add(this.administratorRepository.getMinNumberTransactionsComplaints());
        result.add(this.administratorRepository.getMaxNumberTransactionsComplaints());
        result.add(this.administratorRepository.getStddevNumberTransactionsComplaints());
        return result;
    }

    //Q6
    public List<Double> getSponsorPerEvent(){
        List<Double> result = new ArrayList<>();
        result.add(this.administratorRepository.getAvgNumberSponsorsPerEvent());
        result.add(this.administratorRepository.getMinNumberSponsorsPerEvent());
        result.add(this.administratorRepository.getMaxNumberSponsorsPerEvent());
        result.add(this.administratorRepository.getStddevNumberSponsorsPerEvent());
        return result;
    }

    //Q7
    public Double getRatioOfActiveVSInnactiveSpons(){
        return this.administratorRepository.getRatioOfActiveVSInnactiveSpons();
    }

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

    //Q11
    public Double getRatioOfEmptyVSFullTransactionsComplaints(){
        return this.administratorRepository.getRatioOfEmptyVSFullTransactionsComplaints();
    }

    //Q12
    public Collection<Reader> getTop5ReadersWithMoreComplaints(){
        List<Reader> o = (List<Reader>) this.administratorRepository.getReadersWithMoreComplaints();
        Collection<Reader> result = new ArrayList<>();
        if(o.size()>5){
            result = o.subList(0,5);
        } else result = o;
        return result;
    }

    //Q13
    public Double getRatioOfSalesVSExchangesByReader() {
        return this.administratorRepository.getRatioOfSalesVSExchangesByReader();
    }

    //Q1
    public List<Object> getNumberOfSoldBooksByCategory(String language){
        List<Double> result = new ArrayList<Double>();
        Collection<Category> categories = this.categoryService.findAll();
        List<String> names = new ArrayList<String>();
        List<Object> lists = new ArrayList<Object>();

        for (Category c: categories){
            result.add(this.administratorRepository.getNumberOfSoldBooksByCategory(c));
            if(language.equals("es")){
                names.add(c.getNameEs());
            }else{
                names.add(c.getNameEn());
            }
        }
        lists.add(result);
        lists.add(names);
        return lists;
    }

}
