package controllers.comment;

import controllers.AbstractController;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.*;

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
}
