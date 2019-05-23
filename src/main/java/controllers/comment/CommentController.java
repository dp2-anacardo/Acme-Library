package controllers.comment;

import controllers.AbstractController;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import services.*;

import javax.validation.ValidationException;
import java.util.Collection;

@Controller
@RequestMapping("comment")
public class CommentController extends AbstractController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private RefereeService refereeService;

    @Autowired
    private ReaderService readerService;

    @ExceptionHandler(BindException.class)
    public ModelAndView handleMismatchException(final BindException oops) {
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam int reportId){
        ModelAndView result;
        Collection<Comment> comments;
        Report report;

        try{
            Assert.isTrue(actorService.getActorLogged() instanceof Reader
                    || actorService.getActorLogged() instanceof Referee);
            report = this.reportService.findOne(reportId);

            Assert.isTrue(report.getIsFinal());
            if(this.actorService.getActorLogged() instanceof Referee){
                Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());
                Assert.isTrue(report.getComplaint().getReferee().equals(referee));
            }else {
                Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
                Assert.isTrue(report.getComplaint().getReader().equals(reader));
            }
            comments = report.getComments();
        } catch (Throwable oops){
            result = new ModelAndView("redirect:/report/list.do");
            return result;
        }

        result = new ModelAndView("comment/list");
        result.addObject("comments", comments);
        result.addObject("requestURI", "comment/list.do");
        return result;
    }

    @RequestMapping(value="/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int reportId){
        ModelAndView result;
        Report report;
        Comment comment;

        try{
            report = this.reportService.findOne(reportId);
            Assert.isTrue(report.getIsFinal());

            if(this.actorService.getActorLogged() instanceof Referee){
                Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());
                Assert.isTrue(report.getComplaint().getReferee().equals(referee));
            }else {
                Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
                Assert.isTrue(report.getComplaint().getReader().equals(reader));
            }
            comment = this.commentService.create();
            result = new ModelAndView("comment/create");
            result.addObject("comment", comment);
            result.addObject("reportId", reportId);
        } catch(Throwable oops){
            result = new ModelAndView("redirect:/comment/list.do?reportId="+reportId);
        }
        return result;
    }

    @RequestMapping(value="/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@ModelAttribute("comment") Comment comment, @RequestParam int reportId, BindingResult binding){
        ModelAndView result;

        try{
            comment = this.commentService.reconstruct(comment, binding);
            this.commentService.save(comment, reportId);
            result = new ModelAndView("redirect:/comment/list.do?reportId="+reportId);
        }catch (ValidationException v){
            result = new ModelAndView("comment/create");
            result.addObject("comment", comment);
            result.addObject("reportId", reportId);
        }catch (Throwable oops){
            result = new ModelAndView("comment/create");
            result.addObject("comment", comment);
            result.addObject("reportId", reportId);
            result.addObject("message", "comment.commit.error");
        }
        return result;
    }
}
