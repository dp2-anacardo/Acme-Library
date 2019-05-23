package controllers.offer;

import controllers.AbstractController;
import domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.OfferService;
import services.ReaderService;
import services.TransactionService;
import sun.misc.Request;


@Controller
@RequestMapping("/offer/reader")
public class OfferController extends AbstractController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView listTransactionOffers(@RequestParam int transactionId){
        ModelAndView result;
        try{
            Transaction transaction = this.transactionService.findOne(transactionId);
            Assert.notNull(transaction);
            result = new ModelAndView("offer/reader/list");
            result.addObject("offers",transaction.getOffers());
            result.addObject("bidder",false);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/transaction/listExchanges.do");
        }
        return result;
    }

    @RequestMapping(value = "/listB",method = RequestMethod.GET)
    public ModelAndView listBidderOffers(){
        ModelAndView result;
        try{
            //Coger las offer del que esta loggeado y añadirlas a la vista
            result = new ModelAndView("offer/reader/list");
            result.addObject("bidder",true);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/transaction/listExchanges.do");
        }
        return result;
    }


}
