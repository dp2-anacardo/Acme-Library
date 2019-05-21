
package controllers.referee;

import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Referee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.AdministratorService;
import services.RefereeService;

import javax.validation.Valid;

@Controller
@RequestMapping("referee/referee")
public class EditRefereeController extends AbstractController {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private RefereeService refereeService;




	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final Actor user = this.actorService.getActorLogged();
		final Referee a= this.refereeService.findOne(user.getId());
		Assert.notNull(a);
		result = this.editModelAndView(a);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "update")
	public ModelAndView update(@Valid Referee a, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.editModelAndView(a);
		else
			try {
				a = this.refereeService.reconstruct(a, binding);
				this.refereeService.save(a);
				result = new ModelAndView("redirect:/profile/myInformation.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(a, "actor.commit.error");
			}
		return result;
	}

	protected ModelAndView editModelAndView(final Referee a) {
		ModelAndView result;
		result = this.editModelAndView(a, null);
		return result;
	}

	protected ModelAndView editModelAndView(final Referee a, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("referee/referee/edit");
		result.addObject("referee", a);
		result.addObject("messageCode", messageCode);

		return result;
	}
}
