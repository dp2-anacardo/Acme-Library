package service;

import domain.Actor;
import domain.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.ActorService;
import services.AdministratorService;
import services.MessageService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManageSystemTest extends AbstractTest {

    @Autowired
    private ActorService actorService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private MessageService messageService;

    /*
     * Testing functional requirement : 35.10 An actor who is authenticated as an administrator must be able to launch a process that flags the actor
     * of the system as spammerr or not-spammers
     * Positive: An admin launch the process
     * Negative: A rookie tries to launch the process
     * Sentence coverage: 70.9%
     * Data coverage: Not applicable
     */

    @Test
    public void calculateSpamDriver() {
        Object testingData[][] = {
                {
                        "admin1", null
                }, {
                "referee1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.calculateSpamTemplate((String) testingData[i][0],
                     (Class<?>) testingData[i][1]);
        }
    }

    private void calculateSpamTemplate(String user, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            this.administratorService.computeAllSpam();
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 35.?? An actor who is authenticated as an administrator must be able to launch a process that flags the actor
     * of the system as spammerr or not-spammers
     * Positive: An admin launch the process
     * Negative: A rookie tries to launch the process
     * Sentence coverage: 70.9%
     * Data coverage: Not applicable
     */

    @Test
    public void calculateScoreDriver() {
        Object testingData[][] = {
                {
                        "admin1", null
                }, {
                "organizer1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.calculateScoreTemplate((String) testingData[i][0],
                    (Class<?>) testingData[i][1]);
        }
    }

    private void calculateScoreTemplate(String user, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            this.administratorService.computeAllScores();
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 35.5 An actor who is authenticated as an administrator must be able to ban an actor with the spammer flag
     * Positive: An administrator bans a rookie with spammer flag
     * Negative: An administrator tries to bans a rookie with not-spammer flag
     * Sentence coverage: 93.1%
     * Data coverage: Not applicable
     */

    @Test
    public void banDriver() {
        Object testingData[][] = {
                {
                        "admin1", "reader2", null
                },
                {
                "admin1", "reader1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.banTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (Class<?>) testingData[i][2]);
        }
    }

    private void banTemplate(String admin, String user,  Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(admin);
            final Actor actor = this.actorService.findOne(super.getEntityId(user));
            this.administratorService.ban(actor);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 35.6 An actor who is authenticated as an administrator must be able to unban an actor who was
     * previously banned.
     * Positive: An administrator unbans an actor who was previously banned
     * Negative: An administrator tries to unban an acto who is not banned.
     * Sentence coverage: 100%
     * Data coverage: Not applicable
     */

    @Test
    public void unbanDriver() {
        Object testingData[][] = {
                {
                        "admin1", "reader2", "reader2", null
                }, {
                "admin1", "reader2","reader1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.unbanTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2], (Class<?>) testingData[i][3]);
        }
    }

    private void unbanTemplate(String admin, String ban, String unban,  Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(admin);
            final Actor toBan = this.actorService.findOne(super.getEntityId(ban));
            final Actor toUnban = this.actorService.findOne(super.getEntityId(unban));
            this.administratorService.ban(toBan);
            this.administratorService.unban(toUnban);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 35.9 An actor who is authenticated as an administrator must be able to launch a process that flags the actor
     * of the system as spammerr or not-spammers
     * Positive: An admin launch the process
     * Negative: A rookie tries to launch the process
     * Sentence coverage: 70.9%
     * Data coverage: Not applicable
     */

    @Test
    public void deleteInactiveBooksDriver() {
        Object testingData[][] = {
                {
                        "admin1", null
                }, {
                "referee1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.deleteInactiveBooksTemplate((String) testingData[i][0],
                    (Class<?>) testingData[i][1]);
        }
    }

    private void deleteInactiveBooksTemplate(String user, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            this.administratorService.deleteInactiveBooks();
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 35.8 An actor who is authenticated as an administrator must be able to launch a process that flags the actor
     * of the system as spammerr or not-spammers
     * Positive: An admin launch the process
     * Negative: A rookie tries to launch the process
     * Sentence coverage: 70.9%
     * Data coverage: Not applicable
     */

    @Test
    public void deactivateExpiredCreditCardDriver() {
        Object testingData[][] = {
                {
                        "admin1", null
                }, {
                "referee1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.deactivateExpiredCreditCardTemplate((String) testingData[i][0],
                    (Class<?>) testingData[i][1]);
        }
    }

    private void deactivateExpiredCreditCardTemplate(String user, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            this.administratorService.computeAllSpam();
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 35.3 An actor that is authenticated as a administraotr must be able to broadcast a message
     * Positive: An administrator broadcast a message.
     * Negative: An administrator tries to broadcast a message with invalid fields.
     * Sentence coverage: 92%
     * Data coverage: 50%
     */

    @Test
    public void broadcastDriver() {
        Object testingData[][] = {
                {
                        "admin1", "BR Title 1", "Br Body 1", "HIGH", null
                }, {
                "admin1", "BR Title 1", "", "HIGH", ValidationException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.broadcastTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (String) testingData[i][2],
                    (String) testingData[i][3], (Class<?>) testingData[i][4]);
        }
    }

    private void broadcastTemplate(String user, String title, String body, String priority, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            Message m = this.messageService.create();
            m.setSubject(title);
            m.setBody(body);
            m.setRecipients(this.actorService.findAll());
            m.setPriority(priority);

            final DataBinder binding = new DataBinder(new Message());
            m = this.messageService.reconstruct(m, binding.getBindingResult());

            this.messageService.broadcast(m);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }
}
