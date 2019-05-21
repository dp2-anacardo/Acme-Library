
package controllers.sponsorship;

import controllers.AbstractController;
import domain.Event;
import domain.Sponsor;
import domain.Sponsorship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.ConfigurationService;
import services.EventService;
import services.SponsorshipService;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Date;

@Controller
@RequestMapping("sponsorship")
public class SponsorshipController extends AbstractController {

    @Autowired
    private SponsorshipService sponsorshipService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ConfigurationService configurationService;


    // List -------------------------------------------------------------
    @RequestMapping(value = "sponsor/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        Collection<Sponsorship> sponsorships;

        final Sponsor principal = (Sponsor) this.actorService.getActorLogged();

        sponsorships = this.sponsorshipService.findBySponsor(principal.getId());

        final Date date = new Date();

        result = new ModelAndView("sponsorship/sponsor/list");
        result.addObject("sponsorships", sponsorships);
        result.addObject("date", date);
        result.addObject("requestURI", "sponsorship/sponsor/list.do");

        return result;
    }

    // Create ---------------------------------------------------------------
    @RequestMapping(value = "sponsor/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView result;
        Sponsorship sponsorship;

        sponsorship = this.sponsorshipService.create();
        result = this.createModelAndView(sponsorship);

        return result;
    }

    // Update -------------------------------------------------------------
    @RequestMapping(value = "sponsor/update", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam final int sponsorshipId) {
        ModelAndView result;
        final Sponsorship sponsorship;

        try {
            final Sponsor principal = (Sponsor) this.actorService.getActorLogged();
            sponsorship = this.sponsorshipService.findOne(sponsorshipId);
            Assert.isTrue(sponsorship.getSponsor().equals(principal));
            result = this.updateModelAndView(sponsorship);
        } catch (final Throwable e) {
            result = new ModelAndView("redirect:/");
        }

        return result;
    }

    // Save -------------------------------------------------------------
    @RequestMapping(value = "sponsor/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(Sponsorship sponsorship, final BindingResult binding) {
        ModelAndView result;

        try {
            sponsorship = this.sponsorshipService.reconstruct(sponsorship, binding);
            sponsorship = this.sponsorshipService.save(sponsorship);
            result = new ModelAndView("redirect:list.do");
        } catch (final ValidationException e) {
            result = this.createModelAndView(sponsorship, null);
        } catch (final Throwable oops) {
            result = this.createModelAndView(sponsorship, "sponsorship.commit.error");
        }
        return result;
    }

    // Update Save -------------------------------------------------------------
    @RequestMapping(value = "sponsor/update", method = RequestMethod.POST, params = "update")
    public ModelAndView updateSave(@ModelAttribute("sponsorship") Sponsorship sponsorship, final BindingResult binding) {
        ModelAndView result;

        try {
            sponsorship = this.sponsorshipService.reconstruct(sponsorship, binding);
            sponsorship = this.sponsorshipService.save(sponsorship);
            result = new ModelAndView("redirect:list.do");
        } catch (final ValidationException e) {
            result = this.updateModelAndView(sponsorship, null);
        } catch (final Throwable oops) {
            result = this.updateModelAndView(sponsorship, "sponsorship.commit.error");
        }
        return result;
    }

    // Show ------------------------------------------------------
    @RequestMapping(value = "/sponsor/show", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam final int sponsorshipId) {
        ModelAndView result;
        final Sponsorship sponsorship;

        try {
            final Sponsor principal = (Sponsor) this.actorService.getActorLogged();
            sponsorship = this.sponsorshipService.findOne(sponsorshipId);
            Assert.isTrue(sponsorship.getSponsor().equals(principal));
            result = new ModelAndView("sponsorship/sponsor/show");
            result.addObject("sponsorship", sponsorship);
        } catch (final Throwable e) {
            result = new ModelAndView("redirect:/");
        }

        return result;
    }

    @RequestMapping(value = "sponsor/activate", method = RequestMethod.GET)
    public ModelAndView activate(@RequestParam final int sponsorshipId) {
        ModelAndView result;

        try {
            final Sponsor principal = (Sponsor) this.actorService.getActorLogged();
            final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
            Assert.isTrue(sponsorship.getSponsor().equals(principal));
            this.sponsorshipService.activate(sponsorship);
            result = new ModelAndView("redirect:list.do");
        } catch (final Exception e) {
            result = new ModelAndView("redirect:/");
        }

        return result;
    }

    @RequestMapping(value = "sponsor/desactivate", method = RequestMethod.GET)
    public ModelAndView desactivate(@RequestParam final int sponsorshipId) {
        ModelAndView result;

        try {
            final Sponsor principal = (Sponsor) this.actorService.getActorLogged();
            final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
            Assert.isTrue(sponsorship.getSponsor().equals(principal));
            this.sponsorshipService.desactivate(sponsorship);
            result = new ModelAndView("redirect:list.do");
        } catch (final Exception e) {
            result = new ModelAndView("redirect:/");
        }

        return result;
    }

    // Ancillary methods ------------------------------------------------------
    protected ModelAndView createModelAndView(final Sponsorship sponsorship) {
        ModelAndView result;

        result = this.createModelAndView(sponsorship, null);

        return result;
    }

    protected ModelAndView createModelAndView(final Sponsorship sponsorship, final String message) {
        ModelAndView result;

        final Collection<Event> eventList = this.eventService.findAll();
        final Collection<String> brandList = this.configurationService.getConfiguration().getBrandName();

        result = new ModelAndView("sponsorship/sponsor/create");

        result.addObject("eventList", eventList);
        result.addObject("sponsorship", sponsorship);
        result.addObject("brandList", brandList);
        result.addObject("message", message);

        return result;
    }

    protected ModelAndView updateModelAndView(final Sponsorship sponsorship) {
        ModelAndView result;

        result = this.updateModelAndView(sponsorship, null);

        return result;
    }

    protected ModelAndView updateModelAndView(final Sponsorship sponsorship, final String message) {
        ModelAndView result;

        final Collection<String> brandList = this.configurationService.getConfiguration().getBrandName();

        result = new ModelAndView("sponsorship/sponsor/update");

        result.addObject("sponsorship", sponsorship);
        result.addObject("brandList", brandList);
        result.addObject("message", message);

        return result;
    }

}
