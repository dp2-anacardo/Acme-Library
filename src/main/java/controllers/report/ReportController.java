package controllers.report;

import controllers.AbstractController;
import domain.Complaint;
import domain.Reader;
import domain.Referee;
import domain.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import services.*;

import javax.validation.ValidationException;
import java.util.Collection;

@Controller
@RequestMapping("report")
public class ReportController extends AbstractController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private RefereeService refereeService;

    @Autowired
    private ReaderService readerService;

    @ExceptionHandler(BindException.class)
    public ModelAndView handleMismatchException(final BindException oops) {
        return new ModelAndView("redirect:/");
    }

    //Un referee puede ver todos sus reports
    @RequestMapping(value="/referee/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView result;
        Collection<Report> reports;

        reports = this.reportService.getReportsByReferee(actorService.getActorLogged().getId());

        result = new ModelAndView("report/referee/list");
        result.addObject("reports", reports);
        result.addObject("requestURI", "report/referee/list.do");

        return result;
    }

    //Un reader puede ver los reports en los que esta involucrado siempre que esten en modo final
    @RequestMapping(value="/reader/list", method = RequestMethod.GET)
    public ModelAndView listReader(@RequestParam int complaintId){
        ModelAndView result;
        Collection<Report> reports;
        Complaint complaint;

        try {
            complaint = this.complaintService.findOne(complaintId);
            Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
            Assert.isTrue(complaint.getReader().equals(reader));

            reports = this.reportService.getReportsFinalByComplaint(complaintId);

            result = new ModelAndView("report/reader/list");
            result.addObject("reports", reports);
            result.addObject("requestURI", "report/reader/list.do?complaintId=" + complaintId);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/complaint/reader/list.do");
        }
        return result;
    }

    @RequestMapping(value="/referee/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int complaintId){
        ModelAndView result;
        Complaint complaint;
        Report report;

        try{
            complaint = this.complaintService.findOne(complaintId);
            Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());
            Assert.isTrue(complaint.getReferee().equals(referee));

            report = this.reportService.create();

            result = new ModelAndView("report/referee/edit");
            result.addObject("report", report);
            result.addObject("complaintId", complaintId);
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/complaint/referee/list.do");
        }
        return result;
    }

    @RequestMapping(value="/referee/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int reportId){
        ModelAndView result;
        Report report;

        try{
            report = this.reportService.findOne(reportId);
            Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());
            Assert.isTrue(report.getComplaint().getReferee().equals(referee));
            Assert.isTrue(!report.getIsFinal());

            result = new ModelAndView("report/referee/edit");
            result.addObject("report", report);
            result.addObject("complaintId", report.getComplaint().getId());
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/complaint/referee/list.do");
        }
        return result;
    }

    @RequestMapping(value="/referee/edit", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("report") Report report, @RequestParam int complaintId, BindingResult binding){
        ModelAndView result;

        try{
            report = this.reportService.reconstruct(report, complaintId, binding);
            this.reportService.save(report, complaintId);
            result = new ModelAndView("redirect:/report/referee/list.do");
        } catch(ValidationException v){
            result = new ModelAndView("report/referee/edit");
            for (final ObjectError e : binding.getAllErrors())
                if (e.getDefaultMessage().equals("URL incorrecta") || e.getDefaultMessage().equals("Invalid URL"))
                    result.addObject("attachmentError", e.getDefaultMessage());
            result.addObject("report", report);
            result.addObject("complaintId", complaintId);
        } catch(Throwable oops){
            result = new ModelAndView("report/referee/edit");
            result.addObject("report", report);
            result.addObject("complaintId", complaintId);
            result.addObject("message", "report.commit.error");
        }
        return result;
    }
}
