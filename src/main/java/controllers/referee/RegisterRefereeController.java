
package controllers.referee;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.RefereeService;
import controllers.AbstractController;
import domain.Referee;
import forms.RefereeForm;

@Controller
@RequestMapping("administrator/referee")
public class RegisterRefereeController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private RefereeService			refereeService;

	@Autowired
	private ActorService			actorService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		RefereeForm refereeForm;
		refereeForm = new RefereeForm();
		result = this.createEditModelAndView(refereeForm);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView save(@Valid final RefereeForm refereeForm, final BindingResult binding) {
		ModelAndView result;
		Referee r;

		if (this.actorService.existUsername(refereeForm.getUsername()) == false) {
			binding.rejectValue("username", "error.username");
			result = this.createEditModelAndView(refereeForm);
		} else if (this.administratorService.checkPass(refereeForm.getPassword(), refereeForm.getConfirmPass()) == false) {
			binding.rejectValue("password", "error.password");
			result = this.createEditModelAndView(refereeForm);
		} else if (binding.hasErrors())
			result = this.createEditModelAndView(refereeForm);
		else
			try {
				r = this.refereeService.reconstruct(refereeForm, binding);
				this.refereeService.save(r);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				if (binding.hasErrors())
					result = this.createEditModelAndView(refereeForm, "error.duplicated");
				result = this.createEditModelAndView(refereeForm, "error.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final RefereeForm refereeForm) {
		ModelAndView result;
		result = this.createEditModelAndView(refereeForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final RefereeForm refereeForm, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("administrator/referee/create");
		result.addObject("refereeForm", refereeForm);
		result.addObject("message", messageCode);

		return result;
	}
}
