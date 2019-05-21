package controllers.transaction;

import controllers.AbstractController;
import domain.Book;
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
import services.ActorService;
import services.BookService;
import services.TransactionService;

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

    /*                                             SALES                                                             */
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
