package controllers.messageBox;

import controllers.AbstractController;
import domain.Actor;
import domain.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.MessageBoxService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/messageBox")
public class MessageBoxController extends AbstractController {

    @Autowired
    private MessageBoxService messageBoxService;

    @Autowired
    private ActorService actorService;


    // List -------------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        final ModelAndView result;
        Collection<MessageBox> messageBoxes;
        final Collection<MessageBox> sons = new ArrayList<MessageBox>();

        messageBoxes = this.messageBoxService.findAllByActor(this.actorService.getActorLogged().getId());

        result = new ModelAndView("messageBox/list");
        result.addObject("messageBoxes", messageBoxes);
        result.addObject("requestURI", "messageBox/list.do");

        return result;
    }

    // Creation ---------------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView result;
        MessageBox messageBox;

        messageBox = this.messageBoxService.create();
        result = this.createEditModelAndView(messageBox);

        return result;
    }

    // Edition -------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam final int messageBoxID) {
        ModelAndView result;
        MessageBox messageBox;

        try {
            final Actor principal = this.actorService.getActorLogged();
            messageBox = this.messageBoxService.findOne(messageBoxID);
            Assert.isTrue(principal.getBoxes().contains(messageBox));
            result = this.createEditModelAndView(messageBox);
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    // Save -------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(final MessageBox messageBox, final BindingResult binding) {
        ModelAndView result;
        MessageBox msgBox;
        MessageBox security;

        try {
            final Actor principal = this.actorService.getActorLogged();
            if (messageBox.getId() != 0) {
                security = this.messageBoxService.findOne(messageBox.getId());
                Assert.isTrue(principal.getBoxes().contains(security));
            }
            msgBox = this.messageBoxService.reconstruct(messageBox, binding);
            this.messageBoxService.save(msgBox);
            result = new ModelAndView("redirect:list.do");
        } catch (final ValidationException e) {
            result = this.createEditModelAndView(messageBox);
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }

        return result;

    }


    // Delete ------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(final MessageBox messageBox, final BindingResult binding) {
        ModelAndView result;
        MessageBox msgBox;

        try {
            final Actor principal = this.actorService.getActorLogged();
            msgBox = this.messageBoxService.findOne(messageBox.getId());
            Assert.isTrue(principal.getBoxes().contains(msgBox));
            Assert.isTrue(msgBox.getMessages().size() == 0);
            this.messageBoxService.delete(msgBox);
            result = new ModelAndView("redirect:list.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }

        return result;
    }

    // Ancillary methods ------------------------------------------------------
    protected ModelAndView createEditModelAndView(final MessageBox messageBox) {
        ModelAndView result;

        result = this.createEditModelAndView(messageBox, null);

        return result;
    }

    protected ModelAndView createEditModelAndView(final MessageBox messageBox, final String message) {
        ModelAndView result;

        if (messageBox.getId() == 0)
            result = new ModelAndView("messageBox/create");
        else
            result = new ModelAndView("messageBox/edit");

        result.addObject("messageBox", messageBox);
        result.addObject("message", message);

        return result;
    }

}
