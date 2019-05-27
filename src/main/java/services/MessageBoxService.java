
package services;

import domain.Actor;
import domain.Message;
import domain.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.MessageBoxRepository;
import security.LoginService;
import security.UserAccount;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class MessageBoxService {

    // Manage Repository
    @Autowired
    private MessageBoxRepository messageBoxRepository;

    // Supporting services
    @Autowired
    private ActorService actorService;

    @Autowired
    private Validator validator;


    // CRUD methods
    public MessageBox create() {
        final MessageBox result = new MessageBox();
        result.setIsSystem(false);
        result.setMessages(new ArrayList<Message>());

        return result;
    }

    public MessageBox findOne(final int messageBoxID) {
        final MessageBox result = this.messageBoxRepository.findOne(messageBoxID);
        Assert.notNull(result);

        return result;
    }

    public Collection<MessageBox> findAll() {
        final Collection<MessageBox> result = this.messageBoxRepository.findAll();
        Assert.notNull(result);

        return result;
    }

    public MessageBox save(final MessageBox messageBox) {
        Assert.notNull(messageBox);
        final MessageBox result;
        final UserAccount userAccount = LoginService.getPrincipal();
        Assert.notNull(userAccount);

        if (messageBox.getId() == 0) {
            final Actor actor = this.actorService.findByUsername(userAccount.getUsername());
            messageBox.setIsSystem(false);
            result = this.messageBoxRepository.save(messageBox);
            actor.getBoxes().add(result);
        } else {
            result = this.messageBoxRepository.save(messageBox);
        }
        return result;
    }

    public MessageBox update(final MessageBox messageBox) {
        Assert.notNull(messageBox);
        Assert.isTrue(this.actorService.getActorLogged().getBoxes().contains(messageBox));

        return this.messageBoxRepository.save(messageBox);
    }

    public void delete(final MessageBox messageBox) {
        Assert.notNull(messageBox);
        Assert.isTrue(messageBox.getIsSystem() == false);

        this.actorService.getActorLogged().getBoxes().remove(messageBox);

        this.messageBoxRepository.delete(messageBox);
    }

    // Other methods

    public Collection<MessageBox> findAllByActor(final int actorID) {
        final Collection<MessageBox> result = this.messageBoxRepository.findAllByActor(actorID);
        Assert.notNull(result);

        return result;
    }

    public MessageBox findOneByActorAndName(final int actorID, final String name) {
        Assert.notNull(name);
        final MessageBox result = this.messageBoxRepository.findOneByActorAndName(actorID, name);
        Assert.notNull(result);

        return result;
    }

    public Collection<MessageBox> createSystemMessageBox() {
        final Collection<MessageBox> result = new ArrayList<MessageBox>();
        final Collection<Message> messages = new ArrayList<Message>();
        final MessageBox in = new MessageBox();
        final MessageBox out = new MessageBox();
        final MessageBox trash = new MessageBox();
        final MessageBox spam = new MessageBox();
        final MessageBox notification = new MessageBox();
        in.setName("INBOX");
        in.setMessages(messages);
        in.setIsSystem(true);

        out.setName("OUTBOX");
        out.setMessages(messages);
        out.setIsSystem(true);

        notification.setName("NOTIFICATIONBOX");
        notification.setMessages(messages);
        notification.setIsSystem(true);

        trash.setName("TRASHBOX");
        trash.setMessages(messages);
        trash.setIsSystem(true);

        spam.setName("SPAMBOX");
        spam.setMessages(messages);
        spam.setIsSystem(true);

        result.add(in);
        result.add(out);
        result.add(trash);
        result.add(spam);
        result.add(notification);

        this.messageBoxRepository.save(in);
        this.messageBoxRepository.save(out);
        this.messageBoxRepository.save(notification);
        this.messageBoxRepository.save(trash);
        this.messageBoxRepository.save(spam);

        return result;
    }

    public MessageBox reconstruct(final MessageBox messageBox, final BindingResult binding) {
        MessageBox result;

        if (messageBox.getId() == 0) {
            result = this.create();
        } else {
            result = this.messageBoxRepository.findOne(messageBox.getId());
        }

        final Boolean exists = exists(messageBox);

        result.setName(messageBox.getName());

        this.validator.validate(result, binding);

        if (exists)
            binding.rejectValue("name", "messageBox.duplicated");

        if (binding.hasErrors()) {
            throw new ValidationException();
        }

        return result;

    }

    public Boolean exists(final MessageBox a) {
        Boolean exist = false;

        final Collection<MessageBox> boxes = this.findAllByActor(this.actorService.getActorLogged().getId());
        for (final MessageBox b : boxes)
            if (a.equals(b)) {
                exist = true;
                break;
            }

        return exist;

    }
}