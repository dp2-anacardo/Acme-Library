package services;

import domain.Complaint;
import domain.Reader;
import domain.Referee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.ComplaintRepository;

import java.util.Collection;

@Service
@Transactional
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private RefereeService refereeService;

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

//    public Complaint save(Complaint complaint, int transactionId){
//        Assert.isTrue(actorService.getActorLogged() instanceof Reader);
//        Complaint result;
//        Transaction transaction = this.transactionService.findOne(transactionId);
//        Reader reader = this.readerService.findOne(actorService.getActorLogged().getId());
//        Assert.isTrue(transaction.getSeller().equals(reader));
//
//        //Habría que comprobar que no se pueda crear en una transaction con estado sin finalizar
//        if(transaction.getIsSale()){
//           Assert.notNull(transaction.getCreditCard());
//           Assert.notNull(transaction.getBuyer());
//        }else{
//            //Habria que comprobar que haya una offer de la transaction con estado accepted
//        }
//
//        result = this.complaintRepository.save(complaint);
//        transaction.getComplaints().add(result);
//
//        return result;
//    }

    public void autoAssign(Complaint complaint){
        Assert.isTrue(actorService.getActorLogged() instanceof Referee);
        Assert.isNull(complaint.getReferee());

        Referee referee = this.refereeService.findOne(actorService.getActorLogged().getId());

        complaint.setReferee(referee);
    }
}
