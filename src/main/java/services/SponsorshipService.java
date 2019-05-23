

package services;

import domain.Actor;
import domain.Message;
import domain.Sponsor;
import domain.Sponsorship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.SponsorshipRepository;

import javax.validation.ValidationException;
import java.util.*;

@Service
@Transactional
public class SponsorshipService {

    // Manage Repository
    @Autowired
    private SponsorshipRepository sponsorshipRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private Validator validator;


    // CRUD methods
    public Sponsorship create() {
        final Sponsorship result = new Sponsorship();

        final Sponsor principal = (Sponsor) this.actorService.getActorLogged();

        result.setSponsor(principal);

        return result;
    }

    public Sponsorship findOne(final int sponsorshipID) {
        final Sponsorship result = this.sponsorshipRepository.findOne(sponsorshipID);
        Assert.notNull(result);

        return result;
    }

    public Collection<Sponsorship> findAll() {
        final Collection<Sponsorship> result = this.sponsorshipRepository.findAll();
        Assert.notNull(result);

        return result;
    }

    public Sponsorship save(final Sponsorship sponsorship) {
        Assert.notNull(sponsorship);
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Sponsor.class, principal);

        final Sponsorship result = this.sponsorshipRepository.save(sponsorship);

        return result;
    }

    public void delete(final Sponsorship sponsorship) {
        Assert.notNull(sponsorship);

        this.sponsorshipRepository.delete(sponsorship);
    }

    // Other business methods
    public Collection<Sponsorship> findBySponsor(final int sponsorId) {
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Sponsor.class, principal);

        return this.sponsorshipRepository.findBySponsor(sponsorId);
    }

    public void activate(final Sponsorship sponsorship) {
        final Date date = new Date();

        Assert.notNull(sponsorship);
        Assert.isTrue(sponsorship.getStatus() == false);
        Assert.isTrue(sponsorship.getCreditCard().getExpirationYear().after(date));

        sponsorship.setStatus(true);

        this.sponsorshipRepository.save(sponsorship);
    }

    public void desactivate(final Sponsorship sponsorship) {
        Assert.notNull(sponsorship);
        Assert.isTrue(sponsorship.getStatus() == true);

        sponsorship.setStatus(false);

        this.sponsorshipRepository.save(sponsorship);
    }

    public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding) {
        Sponsorship result;

        if (sponsorship.getId() == 0) {
            result = this.create();
            result.setEvent(sponsorship.getEvent());

        } else
            result = this.sponsorshipRepository.findOne(sponsorship.getId());

        result.setBanner(sponsorship.getBanner());
        result.setCreditCard(sponsorship.getCreditCard());
        result.setStatus(sponsorship.getStatus());

        this.validator.validate(result, binding);

        final Collection<String> brandNames = this.configurationService.getConfiguration().getBrandName();
        if(!brandNames.contains(result.getCreditCard().getBrandName()))
            binding.rejectValue("creditCard.brandName", "sponsorship.creditCard.brandName.error");

//        (!result.getEvent().getIsFinal())
//            binding.rejectValue("event", "sponsorship.event.error.notFinal");

        final Date date = new Date();
        if(result.getCreditCard().getExpirationYear() != null && result.getCreditCard().getExpirationYear().before(date))
            binding.rejectValue("creditCard.expirationYear", "sponsorship.creditCard.expiration.future");

        if (binding.hasErrors())
            throw new ValidationException();

        return result;

    }

    public List<Sponsorship> findAllByActiveEvent(final int eventID) {
        Assert.notNull(eventID);

        List<Sponsorship> sponsorships;

        sponsorships = this.sponsorshipRepository.findAllByActiveEvent(eventID);

        return sponsorships;
    }

    public List<Sponsorship> findAllByParade(final int eventID) {
        Assert.notNull(eventID);

        List<Sponsorship> sponsorships;

        sponsorships = this.sponsorshipRepository.findAllByEvent(eventID);

        return sponsorships;
    }

    public Collection<Sponsorship> findAllActive() {
        final Collection<Sponsorship> sponsorships = this.sponsorshipRepository.findAllActive();

        return sponsorships;
    }

    public Collection<Sponsorship> findAllExpiredCreditCard() {
        final Collection<Sponsorship> sponsorships = this.sponsorshipRepository.findAllExpiredCreditCard();

        return sponsorships;
    }


    public Sponsorship showSponsorship(final int eventID) {
        Sponsorship result = null;
        final List<Sponsorship> sponsorships = this.findAllByActiveEvent(eventID);
        if (sponsorships.size() > 0) {
            final Random rnd = new Random();
            result = sponsorships.get(rnd.nextInt(sponsorships.size()));

            final Double vat = this.configurationService.getConfiguration().getDefaultVAT();
            final Double fee = this.configurationService.getConfiguration().getFlatFee();
            final Double totalAmount = (vat / 100) * fee + fee;
            final Message message = this.messageService.create();
            final Collection<Actor> recipients = new ArrayList<Actor>();
            recipients.add(result.getSponsor());
            message.setRecipients(recipients);
            message.getTags().add("NOTIFICATION");
            message.getMessageBoxes().add(result.getSponsor().getMessageBox("NOTIFICATIONBOX"));
            message.setSubject("A sponsorship has been shown \n Se ha mostrado un anuncio");
            message.setBody("Se le cargará un importe de: " + totalAmount + "\n Se le cargará un importe de:" + totalAmount);
            this.messageService.save(message);
        }

        return result;
    }

    public Collection<Sponsorship> findAllByEvent(final int eventId){
        Collection<Sponsorship> result = this.sponsorshipRepository.findAllByEvent(eventId);
        return result;
    }
}


