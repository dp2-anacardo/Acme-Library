package service;

import datatype.CreditCard;
import domain.Event;
import domain.Sponsorship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.EventService;
import services.SponsorshipService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManageSponsorshipsTest extends AbstractTest {

    @Autowired
    private SponsorshipService sponsorshipService;

    @Autowired
    private EventService eventService;

    /*
     * Testing functional requirement : 9.2 An actor that is authenticated as a sponsor must be able to manage their sponsorships
     * Positive: A sponsor creates a sponsorship
     * Negative: A sponsor tries to create a sponsorship with invalid data
     * Sentence coverage: 99%
     * Data coverage: 50%
     */

    @Test
    public void createSponsorshipDriver() {
        Object testingData[][] = {
                {
                        "https://www.sponsorship5-link.com", "Sponsor 1", "VISA", 856, "4934124580909324", "2026/10/20", "event1", "sponsor1",true, null
                }, {
                "https://www.sponsorship6-link.com", "Sponsor 2", "VISA", 856, "4934124580909324", "2026/10/20", "event1", "sponsor2",true, null
        }, {
                "", "Sponsor 1", "VISA", 856, "4934124580909324", "2026/10/20", "event1", "sponsor1",true, ValidationException.class
        }, {
                "https://www.sponsorship8-link.com", "Sponsor 2", "VISA", 856, "", "2026/10/20", "event1", "sponsor2",true, ValidationException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.createSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3],
                    (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7]
                    , (Boolean) testingData[i][8],(Class<?>) testingData[i][9]);
        }
    }

    private void createSponsorshipTemplate(String banner, String holder, String brand,
                                           int cvv, String number, String expiration, String event, String sponsor,
                                           Boolean status, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(sponsor);

            Sponsorship p = this.sponsorshipService.create();
            Event ev = this.eventService.findOne(super.getEntityId(event));

            p.setBanner(banner);
            p.setEvent(ev);
            p.setStatus(status);

            final CreditCard c = new CreditCard();
            c.setBrandName(brand);
            c.setCvv(cvv);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            final Date date = sdf.parse(expiration);
            c.setExpirationYear(date);
            c.setHolder(holder);
            c.setNumber(number);

            p.setCreditCard(c);

            final DataBinder binding = new DataBinder(new Sponsorship());
            p = this.sponsorshipService.reconstruct(p, binding.getBindingResult());

            this.sponsorshipService.save(p);

        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }


    /*
     * Testing functional requirement : 9.2 An actor that is authenticated as a sponsor must be able to manage their sponsorships
     * Positive: A sponsor edits a sponsorship
     * Negative: A sponsor tries to edit a sponsorship with invalid data
     * Sentence coverage: 99%
     * Data coverage: 33%
     */

    @Test
    public void editSponsorshipDriver() {
        Object testingData[][] = {
                {
                        "sponsor1", "sponsorship1", "https://www.new-banner.es", null
                }, {
                "sponsor2", "sponsorship2", "", ValidationException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.editSponsorshipTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (String) testingData[i][2],
                    (Class<?>) testingData[i][3]);
        }
    }

    private void editSponsorshipTemplate(String sponsor, String sponsorship, String banner, Class<?> expected) {
        Class<?> caught;
        caught = null;


        try {
            this.authenticate(sponsor);
            Sponsorship p = this.sponsorshipService.findOne(super.getEntityId(sponsorship));
            p.setBanner(banner);
            final DataBinder binding = new DataBinder(new Sponsorship());
            p = this.sponsorshipService.reconstruct(p, binding.getBindingResult());
            this.sponsorshipService.save(p);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

    /*
     * Testing functional requirement : An actor who is authenticated as a sponsor must be able to de-activate his sponsorships.
     * Positive: A sponsor successfully de-activate his sponsorship
     * Negative: A sponsor tries to de-activate a de-activated sponsorship
     * Sentence coverage: 100%
     * Data coverage: Not applicable
     */

    @Test
    public void desactivateSponsorshipDriver() {
        final Object testingData[][] = {
                {
                        "sponsor1", "sponsorship1", null
                }, {
                "sponsor1", "sponsorship1", IllegalArgumentException.class
        }
        };

        for (int i = 0; i < testingData.length; i++)
            this.templateDesactivateSponsorship((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

    }

    public void templateDesactivateSponsorship(final String sponsor, final String sponsorship, final Class<?> expected) {

        Class<?> caught;
        caught = null;
        try {

            super.authenticate(sponsor);

            final Sponsorship sp = this.sponsorshipService.findOne(super.getEntityId(sponsorship));

            this.sponsorshipService.desactivate(sp);

        } catch (final Throwable e) {
            caught = e.getClass();
        }
        super.checkExceptions(expected, caught);
    }



    /*
     * Testing functional requirement : An actor who is authenticated as a sponsor must be able to activate his sponsorships.
     * Positive: A sponsor successfully activate his sponsorship
     * Negative: A sponsor tries to activate an activated sponsorship
     * Sentence coverage: 100%
     * Data coverage: Not applicable
     */

    @Test
    public void activateSponsorshipDriver() {
        final Object testingData[][] = {
                {
                        "sponsor2", "sponsorship2", null
                }, {
                "sponsor1", "sponsorship2", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++)
            this.templateActivateSponsorship((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

    }
    public void templateActivateSponsorship(final String sponsor, final String sponsorship, final Class<?> expected) {

        Class<?> caught;
        caught = null;
        try {

            super.authenticate(sponsor);

            final Sponsorship sp = this.sponsorshipService.findOne(super.getEntityId(sponsorship));

            this.sponsorshipService.activate(sp);

        } catch (final Throwable e) {
            caught = e.getClass();
        }
        super.checkExceptions(expected, caught);
    }





}
