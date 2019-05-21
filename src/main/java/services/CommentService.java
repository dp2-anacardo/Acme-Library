package services;

import domain.Comment;
import domain.Reader;
import domain.Referee;
import domain.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.CommentRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private RefereeService refereeService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private Validator validator;

    public Comment create(){
        Assert.isTrue(actorService.getActorLogged() instanceof Reader
                || actorService.getActorLogged() instanceof Referee);

        Comment result = new Comment();

        return result;
    }

    public Comment findOne(int commentId){
        return this.commentRepository.findOne(commentId);
    }

    public Collection<Comment> findAll(){
        return this.commentRepository.findAll();
    }

    public Comment save(Comment comment, int reportId){
        Assert.isTrue(actorService.getActorLogged() instanceof Reader
                || actorService.getActorLogged() instanceof Referee);

        Report report = this.reportService.findOne(reportId);

        if(actorService.getActorLogged() instanceof Referee){
            Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());
            Assert.isTrue(report.getComplaint().getReferee().equals(referee));
        }else{
            Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
            Assert.isTrue(report.getComplaint().getReader().equals(reader));
        }

        Comment result = this.commentRepository.save(comment);
        report.getComments().add(result);

        return result;
    }

    public Comment reconstruct(final Comment comment, final BindingResult binding) {
        Comment result;

        result = this.create();
        result.setMoment(new Date());
        result.setBody(comment.getBody());
        result.setAuthor(actorService.getActorLogged());

        this.validator.validate(result, binding);
        if (binding.hasErrors())
            throw new ValidationException();
        return result;
    }
}
