
package controllers.reader;

import controllers.AbstractController;
import datatype.CreditCard;
import domain.Reader;
import domain.Referee;
import forms.ReaderForm;
import forms.RefereeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.AdministratorService;
import services.ReaderService;
import services.RefereeService;

import javax.validation.Valid;

;

@Controller
@RequestMapping("/reader")
public class RegisterReaderController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ReaderService readerService;

	@Autowired
	private ActorService			actorService;




	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		ReaderForm r;
		r = new ReaderForm();
		result = this.createEditModelAndView(r);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView save(@Valid final ReaderForm r, final BindingResult binding) {
		ModelAndView result;
		Reader re;

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
				final CreditCard c = new CreditCard();
				c.setBrandName(r.getBrandName());
				c.setCvv(r.getCvv());
				c.setExpirationYear(r.getExpirationYear());
				c.setHolder(r.getHolder());
				c.setNumber(r.getNumber());
				re = this.readerService.reconstruct(r, binding);
				re.setCreditCard(c);
				this.readerService.save(re);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				if (binding.hasErrors())
					result = this.createEditModelAndView(r, "error.duplicated");
				result = this.createEditModelAndView(r, "error.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final ReaderForm r) {
		ModelAndView result;
		result = this.createEditModelAndView(r, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ReaderForm r, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("reader/create");
		result.addObject("readerForm", r);
		result.addObject("message", messageCode);

		return result;
	}
}
