
package controllers.organizer;

import controllers.AbstractController;
import domain.Actor;
import domain.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.OrganizerService;

import javax.validation.Valid;

@Controller
@RequestMapping("organizer/organizer")
public class EditOrganizerController extends AbstractController {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private OrganizerService organizerService;




	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final Actor user = this.actorService.getActorLogged();
		final Organizer a= this.organizerService.findOne(user.getId());
		Assert.notNull(a);
		result = this.editModelAndView(a);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "update")
	public ModelAndView update(@Valid Organizer a, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.editModelAndView(a);
		else
			try {
				a = this.organizerService.reconstruct(a, binding);
				this.organizerService.save(a);
				result = new ModelAndView("redirect:/profile/myInformation.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(a, "actor.commit.error");
			}
		return result;
	}

	protected ModelAndView editModelAndView(final Organizer a) {
		ModelAndView result;
		result = this.editModelAndView(a, null);
		return result;
	}

	protected ModelAndView editModelAndView(final Organizer a, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("organizer/organizer/edit");
		result.addObject("organizer", a);
		result.addObject("messageCode", messageCode);

		return result;
	}
}
