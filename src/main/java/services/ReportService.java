package services;

import domain.Complaint;
import domain.Referee;
import domain.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.ReportRepository;

import javax.transaction.Transactional;
import java.util.Collection;

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

        Report result = this.reportRepository.save(report);

        return result;
    }
}
