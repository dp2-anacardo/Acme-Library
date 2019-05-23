package controllers.transaction;

import controllers.AbstractController;
import domain.Book;
import domain.Reader;
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

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/transaction")
public class TransactionController extends AbstractController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private ConfigurationService configurationService;

    /*                                             SALES                                                             */
    @RequestMapping(value = "/reader/listSales", method = RequestMethod.GET)
    public ModelAndView listSales(){
        ModelAndView result;
        try{
            Collection<Transaction> transactions = this.transactionService.getSalesByReader();
            result = new ModelAndView("transaction/reader/listSales");
            result.addObject("transactions",transactions);
            result.addObject("requestURI","transaction/reader/listSales.do");
            result.addObject("compra",false);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }
    @RequestMapping(value = "/reader/listBuys", method = RequestMethod.GET)
    public ModelAndView listBuys(){
        ModelAndView result;
        try{
            Collection<Transaction> transactions = this.transactionService.getBuysByReader();
            result = new ModelAndView("transaction/reader/listBuys");
            result.addObject("transactions",transactions);
            result.addObject("requestURI","transaction/reader/listBuys.do");
            result.addObject("compra",true);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value="/listSales", method = RequestMethod.GET)
    public ModelAndView listSalesNotRegistered(){
        ModelAndView result;

        Collection<Transaction> transactions = this.transactionService.getSalesWithoutBuyer();

        result = new ModelAndView("transaction/listSales");
        result.addObject("transactions", transactions);
        result.addObject("requestURI", "transaction/listSales.do");

        return result;
    }

    @RequestMapping(value="/listExchanges", method = RequestMethod.GET)
    public ModelAndView listExchangesNotRegistered(){
        ModelAndView result;

        Collection<Transaction> transactions = this.transactionService.getExchanges();

        result = new ModelAndView("transaction/listExchanges");
        result.addObject("transactions", transactions);
        result.addObject("requestURI", "transaction/listExchanges.do");

        return result;
    }


    @RequestMapping(value = "/reader/createSale", method = RequestMethod.GET)
    public ModelAndView createSale(){
        ModelAndView result;
        try{
            Transaction transaction = this.transactionService.create();
            Collection<Book> books = new ArrayList<Book>();
            books = this.bookService.getBooksWithNoTransactionsByReader();
            result = new ModelAndView("transaction/reader/createSale");
            result.addObject("books",books);
            result.addObject("transaction",transaction);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/reader/createSale", method = RequestMethod.POST)
    public ModelAndView saveSale(@ModelAttribute("transaction")Transaction transaction, BindingResult binding) {
        ModelAndView result;
        Collection<Book> books = new ArrayList<Book>();
        books = this.bookService.getBooksWithNoTransactionsByReader();
        try {
            transaction = this.transactionService.reconstructSale(transaction,binding);
            if(transaction.getPrice() == null){
                binding.rejectValue("price","transaction.price.error");
                result = new ModelAndView("transaction/reader/createSale");
                result.addObject("books",books);
            }else{
                this.transactionService.saveSale(transaction);
                result = new ModelAndView("redirect:/transaction/reader/listSales.do");
            }
        }catch (ValidationException oops){
            if(transaction.getPrice() == null) {
                binding.rejectValue("price", "transaction.price.error");
            }
            result = new ModelAndView("transaction/reader/createSale");
            result.addObject("books",books);
        }catch(Throwable oops){
            result = new ModelAndView("transaction/reader/createSale");
            result.addObject("messageCode","transaction.commit.error");
            result.addObject("books",books);
        }
        return result;
    }

    @RequestMapping(value = "/reader/delete", method = RequestMethod.GET)
    public ModelAndView deleteSale(@RequestParam int transactionId){
        ModelAndView result;
        try{
            Transaction transaction = this.transactionService.findOne(transactionId);
            Assert.notNull(transaction);
            this.transactionService.delete(transaction);
            result = new ModelAndView("redirect:/transaction/reader/listSales.do");
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/transaction/reader/listSales.do");
        }
        return result;
    }
    @RequestMapping(value = "/reader/buy", method = RequestMethod.GET)
    public ModelAndView buy(@RequestParam int transactionId){
        ModelAndView result;
        try{
            Transaction transaction = this.transactionService.findOne(transactionId);
            Assert.notNull(transaction);
            Assert.isTrue(transaction.getIsSale() == true && transaction.getIsFinished() == false);
            Assert.isTrue(transaction.getSeller() != this.readerService.findOne(this.actorService.getActorLogged().getId()));
            final Collection<String> brandList = this.configurationService.getConfiguration().getBrandName();
            result = new ModelAndView("transaction/reader/buy");
            result.addObject("brandList", brandList);
            result.addObject("transaction",transaction);
        }catch(Throwable oops) {
            result = new ModelAndView("redirect:/transaction/listSales.do");
        }
        return result;
    }

    @RequestMapping(value = "/reader/buy", method = RequestMethod.POST)
    public ModelAndView saveBuy(@ModelAttribute("transaction") Transaction transaction, BindingResult binding){
        ModelAndView result;
        try{
            transaction = this.transactionService.reconstructSale(transaction,binding);
            Assert.isTrue(transaction.getSeller() != transaction.getBuyer());
            this.transactionService.saveSale(transaction);
            result = new ModelAndView("redirect:/transaction/reader/listBuys.do");
        }catch (ValidationException oops){
            final Collection<String> brandList = this.configurationService.getConfiguration().getBrandName();
            result = new ModelAndView("transaction/reader/buy");
            result.addObject("brandList", brandList);
        }catch (Throwable oops){
            final Collection<String> brandList = this.configurationService.getConfiguration().getBrandName();
            result = new ModelAndView("transaction/reader/buy");
            result.addObject("brandList", brandList);
            result.addObject("messageCode","transaction.commit.error");
        }
        return result;
    }

    @RequestMapping(value = "/reader/showSale", method = RequestMethod.GET)
    public ModelAndView showSaleReader(@RequestParam int transactionId){
        ModelAndView result;
        try{
            Transaction transaction = this.transactionService.findOne(transactionId);
            Assert.notNull(transaction);
            Reader r = this.readerService.findOne(this.actorService.getActorLogged().getId());
            Assert.isTrue(transaction.getSeller() == r || transaction.getBuyer() == r);
            result = new ModelAndView("transaction/reader/showSale");
            result.addObject("t",transaction);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value="/show")
    public ModelAndView showNotRegistered(@RequestParam int transactionId){
        ModelAndView result;
        Transaction transaction;

        try{
            transaction = this.transactionService.findOne(transactionId);

            result = new ModelAndView("transaction/show");
            result.addObject("transaction", transaction);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    /*                                             EXCHANGES                                                          */
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
