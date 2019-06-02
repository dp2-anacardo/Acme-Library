package service;

import datatype.CreditCard;
import domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import services.BookService;
import services.OfferService;
import services.TransactionService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TransactionTest extends AbstractTest {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BookService bookService;
    @Autowired
    private OfferService offerService;

    /*
     * Testing functional requirement : 31.2 An actor that is authenticated as a reader must be able to Buy books and/or send offers for exchanges
     * Positive: A reader tries to create a sale
     * Negative: A reader tries to create a sale with invalid data
     * Sentence coverage: 99%
     * Data coverage: 66%
     */

    @Test
    public void createSaleDriver(){
        Object testingData[][] = {
                {
                        20.99,"book3", "reader1",null
                }, {
                -1.0,"book3", "reader1",ValidationException.class
        }
        };
        for(int i = 0; i < testingData.length; i++){
            this.createSaleTemplate((Double) testingData[i][0],super.getEntityId((String)testingData[i][1]),(String) testingData[i][2],(Class<?>)testingData[i][3]);
        }
    }

    public void createSaleTemplate(Double price, int bookId,String reader,Class<?> expected){
        Class<?> caught;
        caught = null;

        try {
            Book book = this.bookService.findOne(bookId);
            super.authenticate(reader);
            Transaction t = this.transactionService.create();
            t.setPrice(price);
            t.setBook(book);
            DataBinder binding = new DataBinder(new Transaction());
            t = this.transactionService.reconstructSale(t,binding.getBindingResult());
            this.transactionService.saveSale(t);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 31.2 An actor that is authenticated as a reader must be able to Buy books and/or send offers for exchanges
     * Positive: A reader tries to create an exchange
     * Negative: A sponsor tries to create an exchange
     * Sentence coverage: 99%
     * Data coverage: 50%
     */

    @Test
    public void createExchange(){
        Object testingData[][] = {
                {
                        "book3", "reader1",null
                }, {
                "book3","sponsor1",IllegalArgumentException.class
        }
        };
        for(int i = 0; i < testingData.length; i++){
            this.createExchangeTemplate(super.getEntityId((String)testingData[i][0]),(String) testingData[i][1],(Class<?>)testingData[i][2]);
        }
    }

    public void createExchangeTemplate(int bookId,String reader,Class<?> expected){
        Class<?> caught;
        caught = null;

        try {
            Book book = this.bookService.findOne(bookId);
            super.authenticate(reader);
            Transaction t = this.transactionService.create();
            t.setBook(book);
            this.transactionService.saveSale(t);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 31.2 An actor that is authenticated as a reader must be able to Buy books and/or send offers for exchanges
     * Positive: A reader tries to buy a sale
     * Negative: A sponsor tries to buy a sale with invalid data
     * Sentence coverage: 99%
     * Data coverage: 33%
     */

    @Test
    public void buySale(){
        Object testingData[][] = {
                {
                    "transaction3","reader1",ValidationException.class,"Sponsor 1", "asdadas", 856, "4934124580909324", "2026/10/20"

                },{
                    "transaction3", "reader1",null,"Sponsor 1", "VISA", 856, "4934124580909324", "2026/10/20"
                }
        };
        for(int i = 0; i < testingData.length; i++){
            this.buySaleTemplate(super.getEntityId((String)testingData[i][0]),(String) testingData[i][1],(Class<?>)testingData[i][2],(String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5],
                    (String) testingData[i][6], (String) testingData[i][7]);
        }
    }

    private void buySaleTemplate(int entityId, String s, Class<?> expected, String holder, String brand, int cvv, String number, String expiration) {
        Class<?> caught;
        caught = null;

        try {
            super.authenticate(s);
            Transaction transaction = this.transactionService.findOne(entityId);
            final CreditCard c = new CreditCard();
            c.setBrandName(brand);
            c.setCvv(cvv);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            final Date date = sdf.parse(expiration);
            c.setExpirationYear(date);
            c.setHolder(holder);
            c.setNumber(number);
            transaction.setCreditCard(c);
            DataBinder binding = new DataBinder(new Transaction());
            transaction = this.transactionService.reconstructSale(transaction,binding.getBindingResult());
            this.transactionService.saveSale(transaction);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);

    }

    /*
     * Testing functional requirement : 9.2 An actor that is authenticated as a reader must be able to Buy books and/or send offers for exchanges
     * Positive: A reader tries to create an offer
     * Negative: A sponsor tries to create an offer
     * Sentence coverage: 99%
     * Data coverage: 50%
     */

    @Test
    public void createOffer(){
        Object testingData[][] = {
                {
                    "book3","reader1","transaction1", IllegalArgumentException.class

                },{
                "book3","reader1","transaction4",null
        }
        };
        for(int i = 0; i < testingData.length; i++){
            this.createOfferTemplate(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1],super.getEntityId((String) testingData[i][2]),
                    (Class<?>)testingData[i][3] );
        }
    }

    private void createOfferTemplate(int bookId,String reader, int transactionId, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            Book book = this.bookService.findOne(bookId);
            Transaction transaction = this.transactionService.findOne(transactionId);
            super.authenticate(reader);
            Offer o = this.offerService.create();
            o.setBook(book);
            o.setTransaction(transaction);
            DataBinder binding = new DataBinder(new Offer());
            o = this.offerService.reconstruct(o,binding.getBindingResult());
            this.offerService.save(o);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

}
