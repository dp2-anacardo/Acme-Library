package controllers.event;

import controllers.AbstractController;
import domain.Event;
import domain.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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

}
