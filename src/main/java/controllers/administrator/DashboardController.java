package controllers.administrator;

import controllers.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.AdministratorService;

@Controller
@RequestMapping("administrator")
public class DashboardController extends AbstractController {

    @Autowired
    private AdministratorService	administratorService;


    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        final ModelAndView result;

        /* Q8 */
        final Double RatioOfFullFinders = this.administratorService.getRatioOfFullFinders();
        /* Q9 */
        final Double RatioOfEmptyFinders = this.administratorService.getRatioOfEmptyFinders();
        /* Q10 */
        final Double RatioOfEmptyVSFullFinders = this.administratorService.getRatioOfEmptyVSFullFinders();
        /* Q13 */
        final Double RatioOfSalesVSExchanges = this.administratorService.getRatioOfSalesVSExchangesByReader();

        result = new ModelAndView("administrator/dashboard");

        result.addObject("RatioOfFullFinders", RatioOfFullFinders);

        result.addObject("RatioOfEmptyFinders", RatioOfEmptyFinders);

        result.addObject("RatioOfEmptyVSFullFinders", RatioOfEmptyVSFullFinders);

        result.addObject("RatioOfSalesVSExchanges", RatioOfSalesVSExchanges);

        return result;
    }
}
