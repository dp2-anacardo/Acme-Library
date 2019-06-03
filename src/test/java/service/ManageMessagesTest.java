package service;

import domain.Actor;
import domain.Message;
import domain.MessageBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.ActorService;
import services.MessageBoxService;
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

    @Autowired
    private MessageBoxService messageBoxService;

    /*
     * Testing functional requirement : 30.5 An actor must be able to manage his or her message boxes, which includes creating one.
     * Positive: An actor creates a message box
     * Negative: An actor tries to create a message box with invalid attributes.
     * Sentence coverage: 96%
     * Data coverage: 100%
     */

    @Test
    public void createMessageBoxDriver() {
        Object testingData[][] = {
                {
                        "reader1", "Test Box", null
                }, {
                "reader2", "", ValidationException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.createMessageBoxTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (Class<?>) testingData[i][2]);
        }
    }

    private void createMessageBoxTemplate(String user, String name, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);

            MessageBox box = this.messageBoxService.create();
            box.setName(name);

            final DataBinder binding = new DataBinder(new MessageBox());
            box = this.messageBoxService.reconstruct(box, binding.getBindingResult());

            this.messageBoxService.save(box);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 30.5 An actor must be able to manage his or her message boxes, which includes editing one.
     * Positive: An actor edits a message box
     * Negative: An actor tries to edit a system box.
     * Sentence coverage: 96%
     * Data coverage: 66.6%
     */

    @Test
    public void editMessageBoxDriver() {
        Object testingData[][] = {
                {
                        "reader2", "Custom Box", "New Name", null
                }, {
                "reader1", "INBOX", "New Name", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.editMessageBoxTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
        }
    }

    private void editMessageBoxTemplate(String user, String name, String newName, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            Integer id = this.actorService.getActorLogged().getMessageBox(name).getId();
            MessageBox box = this.messageBoxService.create();
            box.setId(id);
            box.setName(newName);

            final DataBinder binding = new DataBinder(new MessageBox());
            box = this.messageBoxService.reconstruct(box, binding.getBindingResult());

            this.messageBoxService.save(box);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 30.5 An actor must be able to manage his or her message boxes, which includes deleting one.
     * Positive: An actor deletes a message box
     * Negative: An actor tries to delete a system box.
     * Sentence coverage: 100%
     * Data coverage: Not applicable.
     */

    @Test
    public void deleteMessageBoxDriver() {
        Object testingData[][] = {
                {
                        "reader2", "Custom Box", null
                }, {
                "reader1", "INBOX", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.deleteMessageBoxTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (Class<?>) testingData[i][2]);
        }
    }

    private void deleteMessageBoxTemplate(String user, String name, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            MessageBox box = this.actorService.getActorLogged().getMessageBox(name);
            this.messageBoxService.delete(box);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 30.4 An actor who is authenticated must be able to exchange messages with other actors and manage them
     * which includes sending them.
     * Positive: An actor sends a message
     * Negative: An actor tries to send a message with invalid data
     * Sentence coverage: 90%
     * Data coverage: 33%
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
     * Testing functional requirement : 30.4 An actor who is authenticated must be able to exchange messages with other actors and manage them
     * which includes deleting them.
     * Positive: An actor deletes a message
     * Negative: An actor tries to delete a message of other actor
     * Sentence coverage: 51%
     * Data coverage: Not applicable.
     */


    @Test
    public void deleteMessageDriver() {
        Object testingData[][] = {
                {
                        "reader1", "message1", "INBOX", null
                }, {
                "organizer1", "message1", "INBOX", IllegalArgumentException.class
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
     * Testing functional requirement : 30.4 An actor who is authenticated must be able to exchange messages with other actors and manage them
     * which includes moving them.
     * Positive: An actor moves a message.
     * Negative: An actor tries to move a message to a system box.
     * Sentence coverage: 88.5%
     * Data coverage: Not applicable.
     */

    @Test
    public void moveMessageDriver() {
        Object testingData[][] = {
                {
                        "reader2", "message1", "OUTBOX", "Custom Box", null
                }, {
                "reader2", "message1", "INBOX", "NOTIFICATIONBOX", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.moveMessageTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
        }
    }

    private void moveMessageTemplate(String user, String message, String srcBox, String dstBox, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            Message m = this.messageService.findOne(super.getEntityId(message));
            MessageBox src = this.actorService.getActorLogged().getMessageBox(srcBox);
            MessageBox dst = this.actorService.getActorLogged().getMessageBox(dstBox);
            this.messageService.moveMessage(m, src, dst);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 30.4 An actor who is authenticated must be able to exchange messages with other actors and manage them
     * which includes moving them.
     * Positive: An actor copies a message
     * Negative: An actor tries to copy a message to a system box.
     * Sentence coverage: 94.8%
     * Data coverage: Not applicable.
     */

    @Test
    public void copyMessageDriver() {
        Object testingData[][] = {
                {
                        "reader2", "message1", "OUTBOX", "Custom Box", null
                }, {
                "reader2", "message1", "INBOX", "NOTIFICATIONBOX", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.copyMessageTemplate((String) testingData[i][0],
                    (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
        }
    }

    private void copyMessageTemplate(String user, String message, String srcBox, String dstBox, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            Message m = this.messageService.findOne(super.getEntityId(message));
            MessageBox src = this.actorService.getActorLogged().getMessageBox(srcBox);
            MessageBox dst = this.actorService.getActorLogged().getMessageBox(dstBox);
            this.messageService.copyMessage(m, src, dst);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

}
