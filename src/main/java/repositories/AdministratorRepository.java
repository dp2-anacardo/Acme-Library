
package repositories;

import domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    /*Q2*/
    @Query("select e.organizer from Event e group by e.organizer order by count(e)")
    Collection<Organizer> getOrganizersWithMoreEvents();

    /*Q3*/
    @Query("select avg(t.price) from Transaction t where t.isSale=TRUE")
    Double getAvgTransactionsPrice();

    @Query("select min(t.price) from Transaction t where t.isSale=TRUE")
    Double getMinTransactionsPrice();

    @Query("select max(t.price) from Transaction t where t.isSale=TRUE")
    Double getMaxTransactionsPrice();

    @Query("select stddev(t.price) from Transaction t where t.isSale=TRUE")
    Double getStddevTransactionsPrice();

    /*Q4*/
    @Query("select avg(1.0*(select count(b) from Book b where b.reader.id = r.id)) from Reader r")
    Double getAvgBooksPerReader();

    @Query("select min(1.0*(select count(b) from Book b where b.reader.id = r.id)) from Reader r")
    Double getMinBooksPerReader();

    @Query("select max(1.0*(select count(b) from Book b where b.reader.id = r.id)) from Reader r")
    Double getMaxBooksPerReader();

    @Query("select stddev(1.0*(select count(b) from Book b where b.reader.id = r.id)) from Reader r")
    Double getStddevBooksPerReader();

    /*Q5*/
    @Query("select avg(t.complaints.size)*1.0 from Transaction t")
    Double getAvgNumberTransactionsComplaints();

    @Query("select min(t.complaints.size)*1.0 from Transaction t")
    Double getMinNumberTransactionsComplaints();

    @Query("select max(t.complaints.size)*1.0 from Transaction t")
    Double getMaxNumberTransactionsComplaints();

    @Query("select stddev(t.complaints.size)*1.0 from Transaction t")
    Double getStddevNumberTransactionsComplaints();

    /*Q6*/
    @Query("select avg(1.0*(select count(s) from Sponsorship s where (s.event.id = e.id and s.status=TRUE))) from Event e")
    Double getAvgNumberSponsorsPerEvent();

    @Query("select min(1.0*(select count(s) from Sponsorship s where (s.event.id = e.id and s.status=TRUE))) from Event e")
    Double getMinNumberSponsorsPerEvent();

    @Query("select max(1.0*(select count(s) from Sponsorship s where (s.event.id = e.id and s.status=TRUE))) from Event e")
    Double getMaxNumberSponsorsPerEvent();

    @Query("select stddev(1.0*(select count(s) from Sponsorship s where (s.event.id = e.id and s.status=TRUE))) from Event e")
    Double getStddevNumberSponsorsPerEvent();

    /*Q7*/
    @Query("select (count(s1)*100.0)/(select count(s2) from Sponsorship s2 where s2.status=FALSE) from Sponsorship s1 where s1.status=TRUE")
    Double getRatioOfActiveVSInnactiveSpons();

    /*Q8*/
    @Query("select (count(f1)*100.0)/(select count(f2) from Finder f2) from Finder f1 where f1.transactions is not empty")
    Double getRatioOfFullFinders();

    /*Q9*/
    @Query("select (count(f1)*100.0)/(select count(f2) from Finder f2) from Finder f1 where f1.transactions is empty")
    Double getRatioOfEmptyFinders();

    /*Q10*/
    @Query("select (count(f1)*100.0)/(select count(f2) from Finder f2 where f2.transactions is not empty) from Finder f1 where f1.transactions is empty")
    Double getRatioOfEmptyVSFullFinders();

    /*Q11*/
    @Query("select (count(t1)*100.0)/(select count(t2) from Transaction t2 where t2.complaints is not empty) from Transaction t1 where t1.complaints is empty")
    Double getRatioOfEmptyVSFullTransactionsComplaints();

    /*Q12*/
    @Query("select c.reader from Complaint c group by c.reader order by count(c)")
    Collection<Reader> getReadersWithMoreComplaints();

    /*Q13*/
    @Query("select (count(t1)*100.0)/(select count(t2) from Transaction t2 where t2.isSale = FALSE) from Transaction t1 where t1.isSale = TRUE")
    Double getRatioOfSalesVSExchangesByReader();

    @Query("select count(t) from Transaction t join t.book.categories c where t.isSale=true and t.isFinished= true and ?1 IN c")
    Double getNumberOfSoldBooksByCategory(Category category);
}
