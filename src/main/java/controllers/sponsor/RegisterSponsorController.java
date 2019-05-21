
package controllers.sponsor;

import controllers.AbstractController;
import datatype.CreditCard;
import domain.Referee;
import domain.Sponsor;
import forms.RefereeForm;
import forms.SponsorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.AdministratorService;
import services.RefereeService;
import services.SponsorService;

import javax.validation.Valid;

;

@Controller
@RequestMapping("/sponsor")
public class RegisterSponsorController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private ActorService			actorService;




	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		SponsorForm sponsorForm;
		sponsorForm = new SponsorForm();
		result = this.createEditModelAndView(sponsorForm);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView save(@Valid final SponsorForm sponsorForm, final BindingResult binding) {
		ModelAndView result;
		Sponsor s;

		if (this.actorService.existUsername(sponsorForm.getUsername()) == false) {
			binding.rejectValue("username", "error.username");
			result = this.createEditModelAndView(sponsorForm);
		} else if (this.administratorService.checkPass(sponsorForm.getPassword(), sponsorForm.getConfirmPass()) == false) {
			binding.rejectValue("password", "error.password");
			result = this.createEditModelAndView(sponsorForm);
		} else if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorForm);
		else
			try {
				final CreditCard c = new CreditCard();
				c.setBrandName(sponsorForm.getBrandName());
				c.setCvv(sponsorForm.getCvv());
				c.setExpirationYear(sponsorForm.getExpirationYear());
				c.setHolder(sponsorForm.getHolder());
				c.setNumber(sponsorForm.getNumber());
				s = this.sponsorService.reconstruct(sponsorForm, binding);
				s.setCreditCard(c);
				this.sponsorService.save(s);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				if (binding.hasErrors())
					result = this.createEditModelAndView(sponsorForm, "error.duplicated");
				result = this.createEditModelAndView(sponsorForm, "error.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorForm sponsorForm) {
		ModelAndView result;
		result = this.createEditModelAndView(sponsorForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorForm sponsorForm, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("sponsor/create");
		result.addObject("sponsorForm", sponsorForm);
		result.addObject("message", messageCode);

		return result;
	}
}
