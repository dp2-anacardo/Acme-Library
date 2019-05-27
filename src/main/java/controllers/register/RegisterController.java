package controllers.register;

import controllers.AbstractController;
import domain.Event;
import domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.ReaderService;
import services.RegisterService;

import java.util.Collection;

@Controller
@RequestMapping("/register")
public class RegisterController extends AbstractController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ReaderService readerService;

    @RequestMapping(value = "/reader/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView result;
        try{
            Reader reader = this.readerService.findOne(this.actorService.getActorLogged().getId());
            Collection<Event> events = this.registerService.getEventsPerReader(reader);
            result = new ModelAndView("register/reader/list");
            result.addObject("events", events);
            result.addObject("reader", reader);
            result.addObject("requestURI", "register/reader/list.do");

        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/reader/checkIn", method = RequestMethod.GET)
    public ModelAndView checkIn(@RequestParam int eventId){
        ModelAndView result;
        try{
            this.registerService.save(eventId);
            //result = new ModelAndView("redirect:list.do");
            result = new ModelAndView("redirect: /event/list.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/reader/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int registerId, @RequestParam int eventId){
        ModelAndView result;
        try{
            this.registerService.cancel(registerId, eventId);
            result = new ModelAndView("redirect:list.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }


}
