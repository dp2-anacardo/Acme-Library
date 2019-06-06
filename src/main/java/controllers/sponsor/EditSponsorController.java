
package controllers.sponsor;

import controllers.AbstractController;
import domain.Actor;
import domain.Sponsor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.SponsorService;

import javax.validation.Valid;

@Controller
@RequestMapping("sponsor/sponsor")
public class EditSponsorController extends AbstractController {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private SponsorService sponsorService;




	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final Actor user = this.actorService.getActorLogged();
		final Sponsor a= this.sponsorService.findOne(user.getId());
		Assert.notNull(a);
		result = this.editModelAndView(a);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "update")
	public ModelAndView update(@Valid Sponsor a, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.editModelAndView(a);
		else
			try {
				a = this.sponsorService.reconstruct(a, binding);
				this.sponsorService.save(a);
				result = new ModelAndView("redirect:/profile/myInformation.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(a, "actor.commit.error");
			}
		return result;
	}

	protected ModelAndView editModelAndView(final Sponsor a) {
		ModelAndView result;
		result = this.editModelAndView(a, null);
		return result;
	}

	protected ModelAndView editModelAndView(final Sponsor a, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("sponsor/sponsor/edit");
		result.addObject("sponsor", a);
		result.addObject("messageCode", messageCode);

		return result;
	}
}
