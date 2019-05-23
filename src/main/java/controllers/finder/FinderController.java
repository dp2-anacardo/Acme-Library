package controllers.finder;


import controllers.AbstractController;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Controller
@RequestMapping("finder/reader")
public class FinderController extends AbstractController {

    @Autowired
    private FinderService finderService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private CategoryService categoryService;


    //LIST RESULTS
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        Collection<Transaction> transactions;
        result = new ModelAndView("finder/reader/list");

        //Sacar finder del Member
        final Actor user = this.actorService.getActorLogged();
        final Reader reader = this.readerService.findOne(user.getId());

        Finder finder;
        finder = reader.getFinder();

        //Borrar si fechaLimite < fechaActual
        final Date fechaActual = new Date();
        final Date lastUpdate = finder.getLastUpdate();
        final int horasDeGuardado = this.configurationService.getConfiguration().getMaxTime();
        final Date fechaLimite = new Date(lastUpdate.getTime() + (horasDeGuardado * 3600000L));

        if (fechaActual.after(fechaLimite)) {
            finder.setTransactions(new ArrayList<Transaction>());
            this.finderService.save(finder);
        }

        transactions = finder.getTransactions();
        result.addObject("transactions", transactions);
        result.addObject("finder", finder);
        result.addObject("requestURI", "finder/reader/list.do");

        return result;
    }
    //EDIT FINDER
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit() {
        ModelAndView result;
        Finder finder;

        final Actor user = this.actorService.getActorLogged();
        final Reader reader = this.readerService.findOne(user.getId());

        finder = reader.getFinder();
        Assert.notNull(finder);
        result = this.createEditModelAndView(finder);

        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@ModelAttribute("finder") Finder finder, final BindingResult binding) {
        ModelAndView result;

        try {
            finder = this.finderService.reconstruct(finder, binding);
            if (binding.hasErrors())
                result = this.createEditModelAndView(finder);
            else {
                this.finderService.save(finder);
                result = new ModelAndView("redirect:/finder/reader/list.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public ModelAndView clear() {
        final ModelAndView result;

        final Actor user = this.actorService.getActorLogged();
        final Reader reader = this.readerService.findOne(user.getId());

        Finder finder;
        finder = reader.getFinder();

        finder.setStatus(null);
        finder.setCategoryName(null);
        finder.setKeyWord(null);

        this.finderService.save(finder);

        result = new ModelAndView("redirect:/finder/reader/edit.do");

        return result;
    }

    //MODEL AND VIEWS
    protected ModelAndView createEditModelAndView(final Finder finder) {
        ModelAndView result;

        result = this.createEditModelAndView(finder, null);

        return result;
    }

    protected ModelAndView createEditModelAndView(final Finder finder, final String messageCode) {
        ModelAndView result;

        String language = LocaleContextHolder.getLocale().getLanguage();
        String[] status = {"VERY GOOD", "GOOD", "BAD", "VERY BAD"};

        result = new ModelAndView("finder/reader/edit");
        result.addObject("finder", finder);
        if (language.equals("es")) result.addObject("cNames", this.categoryService.getNamesEs());
        else result.addObject("cNames", this.categoryService.getNamesEn());
        result.addObject("status", status);
        result.addObject("messageCode", messageCode);

        return result;
    }
}