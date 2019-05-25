package services;

import domain.Complaint;
import domain.Reader;
import domain.Referee;
import domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.ComplaintRepository;

import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

@Service
@Transactional
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private RefereeService refereeService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private Validator validator;

    public Complaint create(){
        Assert.isTrue(this.actorService.getActorLogged() instanceof Reader);

        Complaint result = new Complaint();

        return result;
    }

    public Complaint findOne(int complaintId){
        Complaint result = this.complaintRepository.findOne(complaintId);

        return result;
    }

    public Collection<Complaint> findAll(){
        Collection<Complaint> result = this.complaintRepository.findAll();

        return result;
    }

    public Complaint save(Complaint complaint, int transactionId){
        Assert.isTrue(actorService.getActorLogged() instanceof Reader);
        Complaint result;
        Transaction transaction = this.transactionService.findOne(transactionId);
        Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
        Assert.isTrue(transaction.getSeller().equals(reader) || transaction.getBuyer().equals(reader));
        Assert.isTrue(transaction.getIsFinished());

        result = this.complaintRepository.save(complaint);
        transaction.getComplaints().add(result);

        return result;
    }

    public Complaint autoAssign(Complaint complaint){
        Assert.isTrue(actorService.getActorLogged() instanceof Referee);
        Assert.isNull(complaint.getReferee());

        Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());

        complaint.setReferee(referee);

        return complaint;
    }

    public Complaint saveAutoassign(Complaint complaint){
        complaint = this.complaintRepository.save(complaint);
        return complaint;
    }

    private String tickerGenerator() {
        String dateRes = "";
        String numericRes = "";
        final String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        dateRes = new SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime());

        for (int i = 0; i < 5; i++) {
            final Random random = new Random();
            numericRes = numericRes + alphanumeric.charAt(random.nextInt(alphanumeric.length() - 1));
        }

        return dateRes + "-" + numericRes;
    }

    public Complaint reconstruct(Complaint complaint, BindingResult binding){
        Complaint result;
        Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());

        result = this.create();

        result.setTicker(this.tickerGenerator());
        result.setReferee(complaint.getReferee());
        result.setReader(reader);
        result.setMoment(new Date());
        result.setBody(complaint.getBody());
        result.setAttachments(complaint.getAttachments());

        this.validator.validate(result, binding);
        if (binding.hasErrors())
            throw new ValidationException();
        return result;
    }

    public Collection<Complaint> getComplaintsWithNoReferee(){
        return this.complaintRepository.getComplaintsWithNoReferee();
    }

    public Collection<Complaint> getComplaintsByReferee(int refereeId){
        return this.complaintRepository.getComplaintsByReferee(refereeId);
    }

    public Collection<Complaint> getComplaintsByReader(int readerId){
        return this.complaintRepository.getComplaintsByReader(readerId);
    }

    public void delete(final int complaintId){
        this.complaintRepository.delete(complaintId);
    }
}
