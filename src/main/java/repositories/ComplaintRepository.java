package repositories;

import domain.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    @Query("select c from Complaint c where c.referee is null")
    Collection<Complaint> getComplaintsWithNoReferee();

    @Query("select c from Complaint c where c.referee.id=?1")
    Collection<Complaint> getComplaintsByReferee(int refereeId);

    @Query("select c from Transaction t join t.complaints c where t.seller.id =?1 or t.buyer.id = ?1")
    Collection<Complaint> getComplaintsByReader(int readerId);
}
