
package controllers.organizer;

import controllers.AbstractController;
import datatype.CreditCard;
import domain.Administrator;
import domain.Organizer;
import forms.AdministratorForm;
import forms.OrganizerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.AdministratorService;
import services.OrganizerService;

import javax.validation.Valid;

@Controller
@RequestMapping("/organizer")
public class RegisterOrganizerController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private OrganizerService organizerService;

	@Autowired
	private ActorService			actorService;




	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		OrganizerForm r;
		r = new OrganizerForm();
		result = this.createEditModelAndView(r);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView save(@Valid final OrganizerForm r, final BindingResult binding) {
		ModelAndView result;
		Organizer a;

		if (this.actorService.existUsername(r.getUsername()) == false) {
			binding.rejectValue("username", "error.username");
			result = this.createEditModelAndView(r);
		} else if (this.administratorService.checkPass(r.getPassword(), r.getConfirmPass()) == false) {
			binding.rejectValue("password", "error.password");
			result = this.createEditModelAndView(r);
		} else if (binding.hasErrors())
			result = this.createEditModelAndView(r);
		else
			try {
				a = this.organizerService.reconstruct(r, binding);
				this.organizerService.save(a);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				if (binding.hasErrors())
					result = this.createEditModelAndView(r, "error.duplicated");
				result = this.createEditModelAndView(r, "error.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final OrganizerForm r) {
		ModelAndView result;
		result = this.createEditModelAndView(r, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final OrganizerForm r, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("organizer/create");
		result.addObject("organizerForm", r);
		result.addObject("message", messageCode);

		return result;
	}
}
