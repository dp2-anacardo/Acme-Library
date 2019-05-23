
package repositories;

import domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

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
    /*Necesito los complaints hechos*/

    /*Q12*/
    /*Necesito los complaints hechos*/

    /*Q13*/
    @Query("select (count(t1)*100.0)/(select count(t2) from Transaction t2 where t2.isSale = FALSE) from Transaction t1 where t1.isSale = TRUE")
    Double getRatioOfSalesVSExchangesByReader();
}
