/*
 * WelcomeController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import security.LoginService;
import security.UserAccount;
import services.*;

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private RefereeService refereeService;

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private ReaderService	readerService;

	@Autowired
	private OrganizerService	organizerService;

	@RequestMapping(value = "/myInformation", method = RequestMethod.GET)
	public ModelAndView myInformation() {
		final ModelAndView result = new ModelAndView("profile/myInformation");
		final Actor user = this.actorService.getActorLogged();
		final UserAccount userAccount = LoginService.getPrincipal();

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN")) {
			Administrator administrador1;
			administrador1 = this.administratorService.findOne(user.getId());
			Assert.notNull(administrador1);
			result.addObject("administrator", administrador1);
		}
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("REFEREE")) {
			Referee referee;
			referee = this.refereeService.findOne(user.getId());
			Assert.notNull(referee);
			result.addObject("referee", referee);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("SPONSOR")) {
			Sponsor sponsor;
			sponsor = this.sponsorService.findOne(user.getId());
			Assert.notNull(sponsor);
			result.addObject("sponsor", sponsor);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("READER")) {
			Reader reader;
			reader = this.readerService.findOne(user.getId());
			Assert.notNull(reader);
			result.addObject("reader", reader);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER")) {
			Organizer	organizer;
			organizer = this.organizerService.findOne(user.getId());
			Assert.notNull(organizer);
			result.addObject("organizer", organizer);
		}

		return result;
	}

	@RequestMapping(value = "/deleteInformation", method = RequestMethod.GET)
	public ModelAndView deleteInformation(){
		ModelAndView result;

		try {
			Assert.isTrue(this.actorService.getActorLogged() != null);
			this.actorService.deleteInformation();
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Exception e) {
			return new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/exportJSON", method = RequestMethod.GET)
	public ModelAndView exportJSON () {
		final ModelAndView result = new ModelAndView("profile/exportJSON");
		final Actor user = this.actorService.getActorLogged();
		final UserAccount userAccount = LoginService.getPrincipal();
		final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN")) {
			Administrator administrador1;
			administrador1 = this.administratorService.findOne(user.getId());
			Assert.notNull(administrador1);
			final String json = gson.toJson(administrador1);
			result.addObject("json", json);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("REFEREE")) {
			Referee referee;
			referee = this.refereeService.findOne(user.getId());
			Assert.notNull(referee);
			final String json = gson.toJson(referee);
			result.addObject("json", json);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("SPONSOR")) {
			Sponsor sponsor;
			sponsor = this.sponsorService.findOne(user.getId());
			Assert.notNull(sponsor);
			final String json = gson.toJson(sponsor);
			result.addObject("json", json);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("READER")) {
			Reader reader;
			reader = this.readerService.findOne(user.getId());
			Assert.notNull(reader);
			final String json = gson.toJson(reader);
			result.addObject("json", json);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER")) {
			Organizer organizer;
			organizer = this.organizerService.findOne(user.getId());
			Assert.notNull(organizer);
			final String json = gson.toJson(organizer);
			result.addObject("json", json);
		}

		return result;
	}

	@RequestMapping(value="/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam int actorId){
		ModelAndView result;
		Actor actor;

		try{
			actor = this.actorService.findOne(actorId);

			result = new ModelAndView("profile/show");
			result.addObject("actor", actor);

			return result;
		} catch(Throwable oops){
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

}
