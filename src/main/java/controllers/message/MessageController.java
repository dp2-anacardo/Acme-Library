package controllers.message;

import controllers.AbstractController;
import domain.Actor;
import domain.Message;
import domain.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.MessageBoxService;
import services.MessageService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private MessageBoxService messageBoxService;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Collection.class, "recipients", new CustomCollectionEditor(Collection.class) {

            @Override
            protected Object convertElement(final Object element) {
                Integer id = null;

                if (element instanceof String && !((String) element).equals(""))
                    //From the JSP 'element' will be a String
                    try {
                        id = Integer.parseInt((String) element);
                    } catch (final NumberFormatException e) {
                        System.out.println("Element was " + ((String) element));
                        e.printStackTrace();
                    }
                else if (element instanceof Integer)
                    //From the database 'element' will be a Long
                    id = (Integer) element;

                return id != null ? MessageController.this.actorService.findOne(id) : null;
            }
        });
    }

    // List -------------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam final int messageBoxID) {
        ModelAndView result;
        Collection<Message> messages;
        MessageBox msgBox;

        try {
            final Actor principal = this.actorService.getActorLogged();
            msgBox = this.messageBoxService.findOne(messageBoxID);
            Assert.isTrue(principal.getBoxes().contains(msgBox));

            result = new ModelAndView("message/list");
            messages = this.messageService.findAllByMessageBox(messageBoxID);
            result.addObject("messageBox", messageBoxID);
            result.addObject("messages", messages);
            result.addObject("requestURI", "message/list.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    // Create -----------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView result;
        Message mesage;

        mesage = this.messageService.create();
        result = this.createModelAndView(mesage);

        return result;
    }

    // Create Broadcast ------------------------------------------------------------------------
    @RequestMapping(value = "/broadcast", method = RequestMethod.GET)
    public ModelAndView broadcast() {
        ModelAndView result;
        Message mesage;

        mesage = this.messageService.create();
        result = this.createBroadcastModelAndView(mesage);


        return result;
    }

    // Send Broadcast  -------------------------------------------------------------
    @RequestMapping(value = "administrator/broadcast", method = RequestMethod.POST, params = "send")
    public ModelAndView sendBroadcast(@ModelAttribute("mesage") Message mesage, final BindingResult binding) {
        ModelAndView result;

        try {
            mesage = this.messageService.reconstruct(mesage, binding);
            this.messageService.save(mesage);
            result = new ModelAndView("redirect:/message/list.do");
        } catch (final ValidationException e) {
            result = this.createBroadcastModelAndView(mesage, null);
        } catch (final Throwable oops) {
            result = this.createBroadcastModelAndView(mesage, "message.commit.error");
        }
        return result;
    }

    // Send -------------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@ModelAttribute("mesage") Message mesage, final BindingResult binding) {
        ModelAndView result;
        Message msg;

        try {
            mesage = this.messageService.reconstruct(mesage, binding);
            mesage = this.messageService.save(mesage);
            result = new ModelAndView("redirect:/messageBox/list.do");
        } catch (final ValidationException e) {
            result = this.createModelAndView(mesage, null);
        } catch (final Throwable oops) {
            result = this.createModelAndView(mesage, "message.commit.error");
        }
        return result;
    }

    // Display ---------------------------------------
    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam final int messageID, @RequestParam final int messageBoxID) {
        ModelAndView result;
        Message message;
        MessageBox msgBox;
        Collection<MessageBox> actorBoxes;

        try {
            final Actor principal = this.actorService.getActorLogged();
            msgBox = this.messageBoxService.findOne(messageBoxID);
            message = this.messageService.findOne(messageID);
            Assert.isTrue(principal.getBoxes().contains(msgBox));
            Assert.isTrue(principal.getMessageBox(msgBox.getName()).getMessages().contains(message));
            final Collection<MessageBox> messageBoxes = new ArrayList<MessageBox>();
            actorBoxes = this.messageBoxService.findAllByActor(this.actorService.getActorLogged().getId());
            for (final MessageBox b : actorBoxes)
                if (!b.getMessages().contains(message) && b.getIsSystem() == false)
                    messageBoxes.add(b);

            result = new ModelAndView("message/display");
            result.addObject("messageBoxes", messageBoxes);
            result.addObject("messageBoxID", messageBoxID);
            result.addObject("mesage", message);
            result.addObject("mesageRecipients", message.getRecipients());
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }

        return result;
    }

    // Delete ------------------------------------------------------
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam final int messageID, @RequestParam final int messageBoxID) {
        ModelAndView result;
        MessageBox messageBox;
        Message message;

        try {
            final Actor principal = this.actorService.getActorLogged();
            messageBox = this.messageBoxService.findOne(messageBoxID);
            message = this.messageService.findOne(messageID);
            Assert.isTrue(principal.getBoxes().contains(messageBox));
            Assert.isTrue(principal.getMessageBox(messageBox.getName()).getMessages().contains(message));
            this.messageService.delete(message, messageBox);
            result = new ModelAndView("redirect:/messageBox/list.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }

        return result;
    }

    // Move ------------------------------------------------------
    @RequestMapping(value = "/move", method = RequestMethod.GET)
    public ModelAndView move(@RequestParam final int messageID, @RequestParam final int srcBoxID, @RequestParam final int destBoxID) {
        ModelAndView result;
        MessageBox srcBox;
        MessageBox destBox;
        Message message;

        try {
            final Actor principal = this.actorService.getActorLogged();
            srcBox = this.messageBoxService.findOne(srcBoxID);
            destBox = this.messageBoxService.findOne(destBoxID);
            message = this.messageService.findOne(messageID);
            Assert.isTrue(principal.getBoxes().contains(srcBox));
            Assert.isTrue(principal.getMessageBox(srcBox.getName()).getMessages().contains(message));
            Assert.isTrue(principal.getBoxes().contains(destBox));
            this.messageService.moveMessage(message, srcBox, destBox);
            result = new ModelAndView("redirect:/messageBox/list.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    // Copy ------------------------------------------------------
    @RequestMapping(value = "/copy", method = RequestMethod.GET)
    public ModelAndView copy(@RequestParam final int messageID, @RequestParam final int srcBoxID, @RequestParam final int destBoxID) {
        ModelAndView result;
        MessageBox srcBox;
        MessageBox destBox;
        Message message;

        try {
            final Actor principal = this.actorService.getActorLogged();
            srcBox = this.messageBoxService.findOne(srcBoxID);
            destBox = this.messageBoxService.findOne(destBoxID);
            message = this.messageService.findOne(messageID);
            Assert.isTrue(principal.getBoxes().contains(srcBox));
            Assert.isTrue(principal.getMessageBox(srcBox.getName()).getMessages().contains(message));
            Assert.isTrue(principal.getBoxes().contains(destBox));
            this.messageService.copyMessage(message, srcBox, destBox);
            result = new ModelAndView("redirect:/messageBox/list.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
            return result;
        }

        result.addObject("messageBox", srcBoxID);

        return result;
    }
    // Ancillary methods ------------------------------------------------------

    protected ModelAndView createModelAndView(final Message mesage) {
        ModelAndView result;

        result = this.createModelAndView(mesage, null);

        return result;
    }

    protected ModelAndView createModelAndView(final Message mesage, final String message) {
        ModelAndView result;

        final Collection<Actor> actorList = this.actorService.findAll();

        result = new ModelAndView("message/create");
        result.addObject("mesage", mesage);
        result.addObject("message", message);
        result.addObject("actorList", actorList);

        return result;
    }

    protected ModelAndView createBroadcastModelAndView(final Message mesage) {
        ModelAndView result;

        result = this.createBroadcastModelAndView(mesage, null);

        return result;
    }

    protected ModelAndView createBroadcastModelAndView(final Message mesage, final String message) {
        ModelAndView result;

        result = new ModelAndView("message/administrator/broadcast");
        result.addObject("mesage", mesage);
        result.addObject("message", message);

        return result;
    }
}
