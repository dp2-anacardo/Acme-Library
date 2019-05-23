/*
 * AdministratorController.java
 *
 * Copyright (C) 2018 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Referee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.*;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/administrator")
public class ManageActorsController extends AbstractController {

    @Autowired
    private ActorService actorService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private SponsorService sponsorService;

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private ReaderService readerService;


    @RequestMapping(value = "/management", method = RequestMethod.GET)
    public ModelAndView actorList() {
        ModelAndView result;

        result = new ModelAndView("administrator/management");

        final Collection<Actor> actorList = new ArrayList<Actor>();

        actorList.addAll(this.organizerService.findAll());
        actorList.addAll(this.readerService.findAll());
        actorList.addAll(this.sponsorService.findAll());

        result.addObject("actors", actorList);
        result.addObject("requestURI", "administrator/management.do");

        return result;
    }

    @RequestMapping(value = "/management/calculateSpam", method = RequestMethod.GET)
    public ModelAndView calculateSpam() {
        ModelAndView result;

        try {
            this.administratorService.computeAllSpam();
            result = new ModelAndView("redirect:/administrator/management.do");
        } catch (final Throwable oops) {
            return new ModelAndView("redirect:/");
        }

        return result;
    }

    @RequestMapping(value = "/management/calculateScore", method = RequestMethod.GET)
    public ModelAndView calculateScore() {
        ModelAndView result;

        try {
            this.administratorService.computeAllScores();
            result = new ModelAndView("redirect:/administrator/management.do");
        } catch (final Throwable oops) {
            return new ModelAndView("redirect:/");
        }

        return result;
    }

    @RequestMapping(value = "/management/deactivateExpiredCreditCards", method = RequestMethod.GET)
    public ModelAndView deactivateExpiredCreditCards() {
        ModelAndView result;

        try {
            final Integer deactivateSponsorships = this.administratorService.desactivateExpiredSponsorships();
            //result = new ModelAndView("redirect:/administrator/management.do");

            final Collection<Actor> actorList = new ArrayList<Actor>();

            actorList.addAll(this.organizerService.findAll());
            actorList.addAll(this.readerService.findAll());
            actorList.addAll(this.sponsorService.findAll());

            result = new ModelAndView("administrator/management");

            result.addObject("actors", actorList);
            result.addObject("requestURI", "administrator/management.do");
            result.addObject("deactivateSponsorships", deactivateSponsorships);
        } catch (final Throwable oops) {
            return new ModelAndView("redirect:/");
        }

        return result;
    }

    @RequestMapping(value = "/management/deleteInactiveBooks", method = RequestMethod.GET)
    public ModelAndView deleteInactiveBooks() {
        ModelAndView result;

        try {
            final Integer deletedBooks = this.administratorService.deleteInactiveBooks();
            //result = new ModelAndView("redirect:/administrator/management.do");

            final Collection<Actor> actorList = new ArrayList<Actor>();

            actorList.addAll(this.organizerService.findAll());
            actorList.addAll(this.readerService.findAll());
            actorList.addAll(this.sponsorService.findAll());

            result = new ModelAndView("administrator/management");

            result.addObject("actors", actorList);
            result.addObject("requestURI", "administrator/management.do");
            result.addObject("deletedBooks", deletedBooks);
        } catch (final Throwable oops) {
            return new ModelAndView("redirect:/");
        }

        return result;
    }

    @RequestMapping(value = "/management/ban", method = RequestMethod.GET)
    public ModelAndView ban(@RequestParam final int actorId) {
        ModelAndView result;

        try {
            final Actor actor = this.actorService.findOne(actorId);
            this.administratorService.ban(actor);
            result = new ModelAndView("redirect:/administrator/management.do");
        } catch (final Throwable oops) {
            return new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/management/unban", method = RequestMethod.GET)
    public ModelAndView unban(@RequestParam final int actorId) {
        ModelAndView result;

        try {
            final Actor actor = this.actorService.findOne(actorId);
            this.administratorService.unban(actor);
            result = new ModelAndView("redirect:/administrator/management.do");
        } catch (final Throwable oops) {
            return new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/management/showActor", method = RequestMethod.GET)
    public ModelAndView showMember(@RequestParam final int actorId) {
        ModelAndView result;

        result = new ModelAndView("administrator/management/showActor");

        try {

            final Actor actor = this.actorService.findOne(actorId);
            Assert.notNull(actor);

            result.addObject("actor", actor);

        } catch (final Throwable oops) {
            return new ModelAndView("redirect:/");
        }

        return result;
    }

}
