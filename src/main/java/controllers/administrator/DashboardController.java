package controllers.administrator;

import controllers.AbstractController;
import domain.Organizer;
import domain.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.AdministratorService;

import java.util.Collection;

@Controller
@RequestMapping("administrator")
public class DashboardController extends AbstractController {

    @Autowired
    private AdministratorService	administratorService;


    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        final ModelAndView result;

        /* Q2 */
        Collection<Organizer> Top5OrganizersWithMoreEvents = this.administratorService.getTop5OrganizersWithMoreEvents();

        /* Q3 */
        Double AvgTransactionsPrice = this.administratorService.getTransactionsPrice().get(0);
        Double MinTransactionsPrice = this.administratorService.getTransactionsPrice().get(1);
        Double MaxTransactionsPrice = this.administratorService.getTransactionsPrice().get(2);
        Double StddevTransactionsPrice = this.administratorService.getTransactionsPrice().get(3);

        /* Q4 */
        Double AvgBooksPerReader = this.administratorService.getBooksPerReader().get(0);
        Double MinBooksPerReader = this.administratorService.getBooksPerReader().get(1);
        Double MaxBooksPerReader = this.administratorService.getBooksPerReader().get(2);
        Double StddevBooksPerReader = this.administratorService.getBooksPerReader().get(3);

        /* Q5 */
        Double AvgTransactionsComplaint = this.administratorService.getTransactionsComplaint().get(0);
        Double MinTransactionsComplaint = this.administratorService.getTransactionsComplaint().get(1);
        Double MaxTransactionsComplaint = this.administratorService.getTransactionsComplaint().get(2);
        Double StddevTransactionsComplaint = this.administratorService.getTransactionsComplaint().get(3);

        /* Q6 */
        Double AvgSponsorPerEvent = this.administratorService.getSponsorPerEvent().get(0);
        Double MinSponsorPerEvent = this.administratorService.getSponsorPerEvent().get(1);
        Double MaxSponsorPerEvent = this.administratorService.getSponsorPerEvent().get(2);
        Double StddevSponsorPerEvent = this.administratorService.getSponsorPerEvent().get(3);

        /* Q7 */
        final Double RatioOfActiveVSInnactiveSpons = this.administratorService.getRatioOfActiveVSInnactiveSpons();
        /* Q8 */
        final Double RatioOfFullFinders = this.administratorService.getRatioOfFullFinders();
        /* Q9 */
        final Double RatioOfEmptyFinders = this.administratorService.getRatioOfEmptyFinders();
        /* Q10 */
        final Double RatioOfEmptyVSFullFinders = this.administratorService.getRatioOfEmptyVSFullFinders();
        /* Q11 */
        final Double RatioOfEmptyVSFullTransactionsComplaints = this.administratorService.getRatioOfEmptyVSFullTransactionsComplaints();
        /* Q12 */
        final Collection<Reader> Top5ReadersWithMoreComplaints = this.administratorService.getTop5ReadersWithMoreComplaints();
        /* Q13 */
        final Double RatioOfSalesVSExchanges = this.administratorService.getRatioOfSalesVSExchangesByReader();

        result = new ModelAndView("administrator/dashboard");

        result.addObject("Top5OrganizersWithMoreEvents", Top5OrganizersWithMoreEvents);

        /* Q3 */
        result.addObject("AvgTransactionsPrice" , AvgTransactionsPrice);
        result.addObject("MinTransactionsPrice", MinTransactionsPrice);
        result.addObject("MaxTransactionsPrice", MaxTransactionsPrice);
        result.addObject("StddevTransactionsPrice", StddevTransactionsPrice);

        /* Q4 */
        result.addObject("AvgBooksPerReader", AvgBooksPerReader);
        result.addObject("MinBooksPerReader", MinBooksPerReader);
        result.addObject("MaxBooksPerReader", MaxBooksPerReader);
        result.addObject("StddevBooksPerReader", StddevBooksPerReader);

        /* Q5 */
        result.addObject("AvgTransactionsComplaint", AvgTransactionsComplaint);
        result.addObject("MinTransactionsComplaint", MinTransactionsComplaint);
        result.addObject("MaxTransactionsComplaint", MaxTransactionsComplaint);
        result.addObject("StddevTransactionsComplaint", StddevTransactionsComplaint);

        /* Q6 */
        result.addObject("AvgSponsorPerEvent", AvgSponsorPerEvent);
        result.addObject("MinSponsorPerEvent", MinSponsorPerEvent);
        result.addObject("MaxSponsorPerEvent", MaxSponsorPerEvent);
        result.addObject("StddevSponsorPerEvent", StddevSponsorPerEvent);

        /* Q7 */
        result.addObject("RatioOfActiveVSInnactiveSpons", RatioOfActiveVSInnactiveSpons);

        /* Q8 */
        result.addObject("RatioOfFullFinders", RatioOfFullFinders);

        /* Q9 */
        result.addObject("RatioOfEmptyFinders", RatioOfEmptyFinders);

        /* Q10 */
        result.addObject("RatioOfEmptyVSFullFinders", RatioOfEmptyVSFullFinders);

        /* Q11 */
        result.addObject("RatioOfEmptyVSFullTransactionsComplaints", RatioOfEmptyVSFullTransactionsComplaints);

        /* Q12 */
        result.addObject("Top5ReadersWithMoreComplaints", Top5ReadersWithMoreComplaints);

        /* Q13 */
        result.addObject("RatioOfSalesVSExchanges", RatioOfSalesVSExchanges);

        return result;
    }
}
