package services;

import domain.Complaint;
import domain.Referee;
import domain.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.ReportRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private RefereeService refereeService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private Validator validator;

    public Report create(){
        Assert.isTrue(actorService.getActorLogged() instanceof Referee);

        Report result = new Report();

        return result;
    }

    public Report findOne(int reportId){
        return this.reportRepository.findOne(reportId);
    }

    public Collection<Report> findAll(){
        return this.reportRepository.findAll();
    }

    public Report save(Report report, int complaintId){
        Assert.isTrue(actorService.getActorLogged() instanceof Referee);

        Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());
        Complaint complaint = this.complaintService.findOne(complaintId);

        Assert.isTrue(complaint.getReferee().equals(referee));
        Assert.isTrue(report.getComplaint().equals(complaint));

        Report result = this.reportRepository.save(report);

        return result;
    }

    public Collection<Report> getReportsByReferee(int refereeId){
        return this.reportRepository.getReportsByReferee(refereeId);
    }

    public Collection<Report> getReportsFinalByComplaint(int complaintId){
        return this.reportRepository.getReportsFinalByComplaint(complaintId);
    }

    public Report reconstruct(final Report report, int complaintId, final BindingResult binding) {
        Report result;
        if(report.getId()==0) {
            result = this.create();
            result.setMoment(new Date());
            result.setDescription(report.getDescription());
            result.setIsFinal(report.getIsFinal());
            result.setAttachments(report.getAttachments());
            result.setComplaint(complaintService.findOne(complaintId));
            result.setComments(report.getComments());
        } else{
            result = this.findOne(report.getId());
            result.setDescription(report.getDescription());
            result.setAttachments(report.getAttachments());
            result.setIsFinal(report.getIsFinal());
        }

        this.validator.validate(result, binding);
        if (binding.hasErrors())
            throw new ValidationException();
        return result;
    }
}
