
package controllers.reader;

import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.AdministratorService;
import services.ReaderService;

import javax.validation.Valid;

@Controller
@RequestMapping("reader/reader")
public class EditReaderController extends AbstractController {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private ReaderService readerService;




	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final Actor user = this.actorService.getActorLogged();
		final Reader r= this.readerService.findOne(user.getId());
		Assert.notNull(r);
		result = this.editModelAndView(r);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "update")
	public ModelAndView update(@Valid Reader r, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.editModelAndView(r);
		else
			try {
				r = this.readerService.reconstruct(r, binding);
				this.readerService.save(r);
				result = new ModelAndView("redirect:/profile/myInformation.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(r, "actor.commit.error");
			}
		return result;
	}

	protected ModelAndView editModelAndView(final Reader r) {
		ModelAndView result;
		result = this.editModelAndView(r, null);
		return result;
	}

	protected ModelAndView editModelAndView(final Reader r, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("reader/reader/edit");
		result.addObject("reader", r);
		result.addObject("messageCode", messageCode);

		return result;
	}
}
