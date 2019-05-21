package controllers.transaction;

import controllers.AbstractController;
import domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.TransactionService;

import java.util.Collection;

@Controller
@RequestMapping("/transaction")
public class TransactionController extends AbstractController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/reader/listSales", method = RequestMethod.GET)
    public ModelAndView listSales(){
        ModelAndView result;
        try{
            Collection<Transaction> transactions = this.transactionService.getSalesByReader();
            result = new ModelAndView("transaction/reader/listSales");
            result.addObject("transactions",transactions);
            result.addObject("requestURI","transaction/reader/listSales.do");
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/reader/listExchanges", method = RequestMethod.GET)
    public ModelAndView listExchanges(){
        ModelAndView result;
        try{
            Collection<Transaction> transactions = this.transactionService.getExchangesByReader();
            result = new ModelAndView("transaction/reader/listExchanges");
            result.addObject("transactions",transactions);
            result.addObject("requestURI","transaction/reader/listExchanges.do");
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }
}
