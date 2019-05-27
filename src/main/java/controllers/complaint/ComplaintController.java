package controllers.complaint;

import controllers.AbstractController;
import domain.Complaint;
import domain.Reader;
import domain.Referee;
import domain.Transaction;
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
@RequestMapping("complaint")
public class ComplaintController extends AbstractController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private RefereeService refereeService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private TransactionService transactionService;

    @ExceptionHandler(BindException.class)
    public ModelAndView handleMismatchException(final BindException oops) {
        return new ModelAndView("redirect:/");
    }

    //Lista de todas las complaints que no tiene referee para autoasignarselas
    @RequestMapping(value="referee/listUnassigned", method = RequestMethod.GET)
    public ModelAndView listWithNoReferee(){
        ModelAndView result;
        Collection<Complaint> complaints;
        boolean b = true;

        complaints = this.complaintService.getComplaintsWithNoReferee();
        result = new ModelAndView("complaint/referee/listUnassigned");
        result.addObject("complaints", complaints);
        result.addObject("requestURI", "complaint/referee/listUnassigned.do");
        result.addObject("b", b);

        return result;
    }

    //Lista de todas las complaints de un referee
    @RequestMapping(value="/referee/list", method = RequestMethod.GET)
    public ModelAndView listReferee(){
        ModelAndView result;
        Collection<Complaint> complaints;
        boolean b = false;
        Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());

        complaints = this.complaintService.getComplaintsByReferee(referee.getId());
        result = new ModelAndView("complaint/referee/list");
        result.addObject("complaints", complaints);
        result.addObject("requestURI", "complaint/referee/list.do");
        result.addObject("b", b);

        return result;
    }

    //Lista de todas las complaints de un reader
    @RequestMapping(value="/reader/list", method = RequestMethod.GET)
    public ModelAndView listReader(){
        ModelAndView result;
        Collection<Complaint> complaints;
        Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());

        complaints = this.complaintService.getComplaintsByReader(reader.getId());
        result = new ModelAndView("complaint/reader/list");
        result.addObject("complaints", complaints);
        result.addObject("requesstURI", "complaint/reader/list.do");

        return result;
    }

    @RequestMapping(value="/reader/show", method = RequestMethod.GET)
    public ModelAndView showReader(@RequestParam int complaintId){
        ModelAndView result;
        Complaint complaint;

        try {
            Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
            complaint = this.complaintService.findOne(complaintId);
            Transaction transaction = this.transactionService.getTransactionByComplaint(complaintId);
            Assert.isTrue(transaction.getBuyer().equals(reader) || transaction.getSeller().equals(reader));

            result = new ModelAndView("complaint/reader/show");
            result.addObject("complaint", complaint);
        } catch(Throwable oops){
            result = new ModelAndView("redirect:/complaint/reader/list.do");
        }
        return result;
    }

    @RequestMapping(value="/referee/show", method = RequestMethod.GET)
    public ModelAndView showReferee(@RequestParam int complaintId){
        ModelAndView result;
        Complaint complaint;

        try {
            Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());
            complaint = this.complaintService.findOne(complaintId);
            Assert.isTrue(complaint.getReferee().equals(referee));

            result = new ModelAndView("complaint/referee/show");
            result.addObject("complaint", complaint);
        } catch(Throwable oops){
            result = new ModelAndView("redirect:/complaint/referee/list.do");
        }
        return result;
    }

    @RequestMapping(value="/referee/autoAssign", method = RequestMethod.GET)
    public ModelAndView autoAssign(@RequestParam int complaintId){
        ModelAndView result;
        Complaint complaint;

        try{
            complaint = this.complaintService.findOne(complaintId);
            complaint = this.complaintService.autoAssign(complaint);
            this.complaintService.saveAutoassign(complaint);

            result = new ModelAndView("redirect:/complaint/referee/listUnassigned.do");
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/complaint/referee/listUnassigned.do");
        }
        return result;
    }

    @RequestMapping(value="/reader/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int transactionId){
        ModelAndView result;
        Complaint complaint;
        Transaction transaction;

        try{
            transaction = transactionService.findOne(transactionId);
            Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
            Assert.isTrue(transaction.getBuyer().equals(reader) ||
                    transaction.getSeller().equals(reader));
            Assert.isTrue(transaction.getIsFinished());

            complaint = this.complaintService.create();

            result = new ModelAndView("complaint/reader/create");
            result.addObject("complaint", complaint);
            result.addObject("transactionId", transactionId);
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/transaction/reader/list.do");
        }
        return result;
    }

    @RequestMapping(value="/reader/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@ModelAttribute("complaint") Complaint complaint, @RequestParam int transactionId, BindingResult binding){
        ModelAndView result;

        try{
            complaint = this.complaintService.reconstruct(complaint, binding);
            this.complaintService.save(complaint, transactionId);
            result = new ModelAndView("redirect:/complaint/reader/list.do");
        } catch(ValidationException v){
            result = new ModelAndView("complaint/reader/create");
            for (final ObjectError e : binding.getAllErrors())
                if (e.getDefaultMessage().equals("URL incorrecta") || e.getDefaultMessage().equals("Invalid URL"))
                    result.addObject("attachmentError", e.getDefaultMessage());
            result.addObject("complaint", complaint);
            result.addObject("transactionId", transactionId);
        } catch(Throwable oops){
            result = new ModelAndView("complaint/reader/create");
            result.addObject("complaint", complaint);
            result.addObject("transactionId", transactionId);
            result.addObject("message", "complaint.commit.error");
        }
        return result;
    }

}
