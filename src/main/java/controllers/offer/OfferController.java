package controllers.offer;

import controllers.AbstractController;
import domain.Book;
import domain.Offer;
import domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.*;
import sun.misc.Request;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;


@Controller
@RequestMapping("/offer/reader")
public class OfferController extends AbstractController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private BookService bookService;

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
            Collection<Offer> offers = this.offerService.getOffersByReader();
            result = new ModelAndView("offer/reader/list");
            result.addObject("bidder",true);
            result.addObject("offers",offers);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/transaction/listExchanges.do");
        }
        return result;
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int transactionId){
        ModelAndView result;
        try{
            Transaction transaction = this.transactionService.findOne(transactionId);
            Assert.notNull(transaction);
            Assert.isTrue(transaction.getSeller() != this.readerService.findOne(this.actorService.getActorLogged().getId()));
            Offer offer = this.offerService.create();
            offer.setTransaction(transaction);
            Collection<Book> books = new ArrayList<Book>();
            books = this.bookService.getBooksWithNoTransactionsByReader();
            result = new ModelAndView("offer/reader/create");
            result.addObject("offer",offer);
            result.addObject("books",books);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/transaction/listExchanges.do");
        }
        return result;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("offer") Offer offer, BindingResult binding){
        ModelAndView result;
        try{
            offer = this.offerService.reconstruct(offer,binding);
            this.offerService.save(offer);
            result = new ModelAndView("redirect:/offer/reader/listB.do");
        }catch (ValidationException oops){
            Collection<Book> books = new ArrayList<Book>();
            books = this.bookService.getBooksWithNoTransactionsByReader();
            result = new ModelAndView("offer/reader/create");
            result.addObject(books);
        }catch (Throwable oops){
            Collection<Book> books = new ArrayList<Book>();
            books = this.bookService.getBooksWithNoTransactionsByReader();
            result = new ModelAndView("offer/reader/create");
            result.addObject("message","transaction.commit.error");
            result.addObject("books",books);
        }
        return result;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam int offerId){
        ModelAndView result;
        try{
            Offer offer = this.offerService.findOne(offerId);
            Assert.notNull(offer);
            Assert.isTrue(offer.getTransaction().getSeller() == this.readerService.findOne(this.actorService.getActorLogged().getId()) || offer.getReader()==this.readerService.findOne(this.actorService.getActorLogged().getId()) );
            result = new ModelAndView("offer/reader/show");
            if(offer.getTransaction().getSeller() == this.readerService.findOne(this.actorService.getActorLogged().getId())){
                result.addObject("seller",true);
            }else{
                result.addObject("seller",false);
            }
            result.addObject("o",offer);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/accept", method = RequestMethod.GET)
    public ModelAndView accept(@RequestParam int offerId){
        ModelAndView result;
        try{
            Offer offer = this.offerService.findOne(offerId);
            this.offerService.accept(offer);
            result = new ModelAndView("redirect:/transaction/reader/listExchanges.do");
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }
    @RequestMapping(value = "/reject", method = RequestMethod.GET)
    public ModelAndView reject(@RequestParam int offerId){
        ModelAndView result;
        try{
            Offer offer = this.offerService.findOne(offerId);
            this.offerService.reject(offer);
            result = new ModelAndView("redirect:/transaction/reader/listExchanges.do");
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }


}
