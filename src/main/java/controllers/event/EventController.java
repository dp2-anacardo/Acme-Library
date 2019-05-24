package controllers.event;

import controllers.AbstractController;
import domain.Event;
import domain.Register;
import org.hibernate.metamodel.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.EventService;

import java.util.Collection;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ActorService actorService;


    @RequestMapping(value = "/organizer/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView result;
        try{
            int organizerId = this.actorService.getActorLogged().getId();
            Collection<Event> events = this.eventService.getEventsPerOrOrganizer(organizerId);
            Assert.notNull(events);
            result = new ModelAndView("event/organizer/list");
            result.addObject("events", events);
            result.addObject("requestURI", "event/organizer/list.do");

        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/organizer/show", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam int eventId){
        ModelAndView result;
        try{
            int organizerId = this.actorService.getActorLogged().getId();
            Event event = this.eventService.findOne(eventId);
            Assert.notNull(event);
            Assert.isTrue(event.getOrganizer().getId() == organizerId);
            Collection<Register> registers = event.getRegisters();
            result = new ModelAndView("event/organizer/show");
            result.addObject("event", event);
            result.addObject("registers", registers);
            result.addObject("requestURI", "event/organizer/show.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/organizer/create", method = RequestMethod.GET)
    public ModelAndView create(){
        ModelAndView result;
        try{
            Event event = this.eventService.create();
            result = new ModelAndView("event/organizer/create");
            result.addObject("event", event);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/organizer/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int eventId){
        ModelAndView result;
        try{
            Event event;
            event = this.eventService.findOne(eventId);
            Assert.notNull(event);
            Assert.isTrue(event.getIsFinal() == false);
            int organizerId = this.actorService.getActorLogged().getId();
            Assert.isTrue(event.getOrganizer().getId() == organizerId);
            result = new ModelAndView("event/organizer/edit");
            result.addObject("event",event);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/organizer/edit", method = RequestMethod.POST, params = "saveDraft")
    public ModelAndView saveDraft(Event event, BindingResult binding){
        ModelAndView result;
        try{
            Assert.notNull(event);
            event = this.eventService.reconstruct(event, binding);
            event = this.eventService.saveDraft(event);
            result = new ModelAndView("redirect:list.do");
        }catch (ValidationException e){
            result = this.createEditModelAndView(event, null);
        } catch (Throwable oops){
            result = this.createEditModelAndView(event, "event.commit.error");
        }
        return result;
    }

    @RequestMapping(value = "/organizer/edit", method = RequestMethod.POST, params = "saveFinal")
    public ModelAndView saveFinal(Event event, BindingResult binding){
        ModelAndView result;
        try{
            Assert.notNull(event);
            event = this.eventService.reconstruct(event, binding);
            event = this.eventService.saveFinal(event);
            result = new ModelAndView("redirect:list.do");
        }catch (ValidationException e){
            result = this.createEditModelAndView(event, null);
        } catch (Throwable oops){
            result = this.createEditModelAndView(event, "event.commit.error");
        }
        return result;
    }

    @RequestMapping(value = "/organizer/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int eventId){
        ModelAndView result;
        try{
            Event event = this.eventService.findOne(eventId);
            Assert.notNull(event);
            this.eventService.delete(event);
            result = new ModelAndView("redirect:list.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    private ModelAndView createEditModelAndView(Event event) {
        ModelAndView result;
        result = this.createEditModelAndView(event, null);
        return result;
    }

    private ModelAndView createEditModelAndView(Event event, final String messageCode) {
        ModelAndView result;

        result = new ModelAndView("event/organizer/edit");
        result.addObject("event", event);
        result.addObject("message", messageCode);
        return result;
    }

}
