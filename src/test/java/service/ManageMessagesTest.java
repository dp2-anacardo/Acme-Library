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
import services.MessageService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManageMessagesTest extends AbstractTest {

    @Autowired
    private ActorService actorService;

    @Autowired
    private MessageService messageService;


    /*
     * Testing functional requirement : 23.2 An actor that is authenticated must be able to manage their messages
     * Positive: An actor sends a message
     * Negative: An actor tries to send a message with invalid data
     * Sentence coverage: 84%
     * Data coverage: 20%
     */

    @Test
    public void createMessageDriver() {
        Object testingData[][] = {
                {
                        "reader1", "This is the subject 1", "This is the body 1", "TAG1", "admin1", "HIGH", null
                }, {
                "reader2", "", "This is the body 2", "", "organizer1", "MEDIUM", ValidationException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.createMessageTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4]
                    , (String) testingData[i][5], (Class<?>) testingData[i][6]);
        }
    }

    private void createMessageTemplate(String sender, String subject, String body,
                                       String tag, String recipient, String priority, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(sender);

            Message m = this.messageService.create();
            m.setSender(this.actorService.getActorLogged());
            m.setSubject(subject);
            m.setBody(body);
            m.getTags().add(tag);
            m.setPriority(priority);
            Collection<Actor> recipients = new ArrayList<Actor>();
            recipients.add(this.actorService.findOne(super.getEntityId(recipient)));
            m.setRecipients(recipients);

            final DataBinder binding = new DataBinder(new Message());
            m = this.messageService.reconstruct(m, binding.getBindingResult());

            this.messageService.save(m);

        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }


    /*
     * Testing functional requirement : 23.2 An actor that is authenticated must be able to manage their messages
     * Positive: An actor deletes a message
     * Negative: A company tries to delete a message that not exists
     * Sentence coverage: 75.5%
     * Data coverage: Not applicable
     */

    @Test
    public void deleteMessageDriver() {
        Object testingData[][] = {
                {
                        "reader1", "message1", "OUTBOX", null
                }, {
                "reader2", "message2", "INBOX", AssertionError.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.deleteMessageTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
        }
    }

    private void deleteMessageTemplate(String user, String message, String box, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            Message m = this.messageService.findOne(super.getEntityId(message));
            this.messageService.delete(m, this.actorService.getActorLogged().getMessageBox(box));
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 24.1 An actor that is authenticated as a administraotr must be able to broadcast a message
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
