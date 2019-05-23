package services;

import domain.Actor;
import domain.Configuration;
import domain.Message;
import domain.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

@Service
@Transactional
public class MessageService {

    // Manage Repository
    @Autowired
    private MessageRepository messageRepository;

    //Supporting devices
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private Validator validator;


    // CRUD methods
    public Message create() {
        final Message result = new Message();
        final Calendar calendar = new GregorianCalendar();
        final long time = calendar.getTimeInMillis() - 500;
        calendar.setTimeInMillis(time);
        final Collection<String> tags = new ArrayList<String>();
        final Collection<MessageBox> boxes = new ArrayList<MessageBox>();

        result.setTags(tags);
        result.setMoment(calendar.getTime());
        result.setMessageBoxes(boxes);

        return result;
    }

    public Message findOne(final int messageID) {
        final Message result = this.messageRepository.findOne(messageID);
        Assert.notNull(result);

        return result;
    }

    public Message save(final Message message) {
        Assert.notNull(message);
        final Message result;

        if (message.getId() == 0) {
            Assert.notNull(message);

            final Actor sender = this.actorService.getActorLogged();

            message.setSender(sender);

            final Collection<Actor> recipients = message.getRecipients();
            Assert.notNull(recipients);
            Assert.notEmpty(recipients);

            final Boolean spam = this.checkSpam(message);
            final String box;

            if (spam) {
                box = "SPAMBOX";
                message.setIsSpam(true);
            } else {
                box = "INBOX";
                message.setIsSpam(false);
            }

            message.getMessageBoxes().add(sender.getMessageBox("OUTBOX"));
            for (final Actor recipient : recipients)
                message.getMessageBoxes().add(recipient.getMessageBox(box));

            result = this.messageRepository.save(message);

            sender.getMessageBox("OUTBOX").addMessage(result);
            for (final Actor recipient : recipients)
                recipient.getMessageBox(box).addMessage(result);
        } else
            result = this.messageRepository.save(message);

        return result;
    }

    public Message broadcast(final Message message){
        final Message result;

        final Collection<Actor> recipients = message.getRecipients();
        Assert.notNull(recipients);
        Assert.notEmpty(recipients);


        for (final Actor recipient : recipients)
            message.getMessageBoxes().add(recipient.getMessageBox("NOTIFICATIONBOX"));

        result = this.messageRepository.save(message);

        for (final Actor recipient : recipients)
            recipient.getMessageBox("NOTIFICATIONBOX").addMessage(result);

        return result;
    }

    public Message notification(){
        //            final String acceptedEnrolment = "Enrolment accepted \n Inscripción aceptada";
//            final String dropoutBrotherhood = "Drop out brotherhood \n Salida de fraternidad";
//            final String acceptedRequest = "A request changed its status \n Una petición ha cambiado su estatus";
//            final String sponsorshipFee = "A sponsorship has been shown \n Se ha mostrado un anuncio";
//            final Integer actors = this.actorService.findAll().size();
//
//            if (message.getSubject().equals(sponsorshipFee) || message.getSubject().equals(acceptedRequest) || message.getSubject().equals(acceptedEnrolment) || message.getSubject().equals(dropoutBrotherhood)
//                    || (message.getRecipients().size() == actors)) {
//
//                final Collection<Actor> recipients = message.getRecipients();
//                Assert.notNull(recipients);
//                Assert.notEmpty(recipients);
//
//                for (final Actor recipient : recipients)
//                    message.getMessageBoxes().add(recipient.getMessageBox("NOTIFICATIONBOX"));
//
//                result = this.messageRepository.save(message);
//
//                for (final Actor recipient : recipients)
//                    recipient.getMessageBox("NOTIFICATIONBOX").addMessage(result);
//
//            } else {
        return null;
    }

    public void delete(final Message message, final MessageBox srcMessageBox) {
        Assert.notNull(message);
        final UserAccount userAccount = LoginService.getPrincipal();
        Assert.notNull(userAccount);
        final Actor actor = this.actorService.findByUsername(userAccount.getUsername());

        Assert.isTrue(message.getRecipients().contains(actor) || message.getSender().equals(actor));

        if (srcMessageBox.getName().equals("TRASHBOX")) {
            for (final MessageBox box : actor.getBoxes())
                if (box.getMessages().contains(message)) {
                    actor.getMessageBox(box.getName()).deleteMessage(message);
                    message.getMessageBoxes().remove(box);
                }

            if (message.getMessageBoxes().size() == 0) //&& message.getSender() == null message.getRecipients().size() == 0 &&
                this.messageRepository.delete(message);
            else
                this.messageRepository.save(message);

        } else {
            Assert.isTrue(srcMessageBox.getMessages().contains(message));
            this.moveMessage(message, srcMessageBox, actor.getMessageBox("TRASHBOX"));
        }
    }

    public void deleteAll(final Message m) {

        this.messageRepository.delete(m);

    }
    // Other methods

    public Message moveMessage(final Message message, final MessageBox srcMessageBox, final MessageBox destMessageBox) {
        Assert.notNull(message);
        Assert.notNull(srcMessageBox);
        Assert.notNull(destMessageBox);
        Assert.notNull(LoginService.getPrincipal());

        final Actor actor = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
        Assert.isTrue(actor.getBoxes().contains(srcMessageBox));
        Assert.isTrue(actor.getBoxes().contains(destMessageBox));

        Assert.isTrue(actor.getMessageBox(srcMessageBox.getName()).getMessages().contains(message));

        Assert.isTrue(message.getRecipients().contains(actor) || message.getSender().equals(actor));
        Assert.isTrue(message.getMessageBoxes().contains(srcMessageBox));

        if (!destMessageBox.getMessages().contains(message)) {
            message.getMessageBoxes().remove(srcMessageBox);
            message.getMessageBoxes().add(destMessageBox);
            srcMessageBox.deleteMessage(message);
            destMessageBox.addMessage(message);
        } else {
            message.getMessageBoxes().remove(srcMessageBox);
            srcMessageBox.deleteMessage(message);
        }

        return this.messageRepository.save(message);
    }

    public Message copyMessage(final Message message, final MessageBox srcMessageBox, final MessageBox destMessageBox) {
        Assert.notNull(message);
        Assert.notNull(srcMessageBox);
        Assert.notNull(destMessageBox);
        Assert.notNull(LoginService.getPrincipal());

        final Actor actor = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
        Assert.isTrue(actor.getBoxes().contains(srcMessageBox));
        Assert.isTrue(actor.getBoxes().contains(destMessageBox));

        Assert.isTrue(actor.getMessageBox(srcMessageBox.getName()).getMessages().contains(message));
        Assert.isTrue(!actor.getMessageBox(destMessageBox.getName()).getMessages().contains(message));

        Assert.isTrue(message.getRecipients().contains(actor) || message.getSender().equals(actor));
        Assert.isTrue(message.getMessageBoxes().contains(srcMessageBox));

        message.getMessageBoxes().add(destMessageBox);
        destMessageBox.addMessage(message);

        return this.messageRepository.save(message);
    }

    // Aux methods
    private Boolean checkSpam(final Message message) {
        Boolean spam = false;

        final Configuration configuration = this.configurationService.findAll().get(0);
        final Collection<String> spamWords = configuration.getSpamWords();
        for (final String word : spamWords)
            if (message.getSubject().contains(word)) {
                spam = true;
                break;
            }
        if (!spam)
            for (final String word : spamWords)
                if (message.getBody().contains(word)) {
                    spam = true;
                    break;
                }

        return spam;
    }

    public Collection<Message> findAllByMessageBox(final int messageBoxID) {
        final Collection<Message> result = this.messageRepository.findByMessageBox(messageBoxID);
        Assert.notNull(result);

        return result;
    }

    public Collection<Message> findAllSentByActor(final int actorID) {
        final Collection<Message> result = this.messageRepository.findAllSentByActor(actorID);
        Assert.notNull(result);

        return result;
    }

    public Double findSpamRatioByActor(final int actorID) {
        Double result = this.messageRepository.findSpamRatioByActor(actorID);
        if (result == null)
            result = 0.0;

        return result;
    }

    public Message reconstruct(final Message message, final BindingResult binding) {

        final Message result;

        result = this.create();

        result.setSubject(message.getSubject());
        result.setPriority(message.getPriority());
        result.setBody(message.getBody());
        result.setTags(message.getTags());
        result.setRecipients(message.getRecipients());

        this.validator.validate(result, binding);

        if (binding.hasErrors())
            throw new ValidationException();

        return result;
    }

    public Collection<Message> findAllReceivedByActor (final int actorID){
        Collection<Message> result = this.messageRepository.findAllReceivedByActor(actorID);
        return result;
    }
}