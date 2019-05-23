package repositories;

import domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("select r from Report r join r.complaint c join c.referee referee where referee.id=?1")
    Collection<Report> getReportsByReferee(int refereeId);

    @Query("select r from Report r join r.complaint c where r.isFinal=true and c.id=?1")
    Collection<Report> getReportsFinalByComplaint(int readerId);
}
