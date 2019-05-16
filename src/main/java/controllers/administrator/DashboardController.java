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

        result = new ModelAndView("administrator/dashboard");

        return result;
    }
}
