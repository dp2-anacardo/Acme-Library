
package repositories;

import domain.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

    @Query("select s from Sponsorship s where s.id = ?1")
    Collection<Sponsorship> findBySponsor(int sponsorId);

    @Query("select s from Sponsorship s where s.event.id = ?1 and s.status = TRUE")
    List<Sponsorship> findAllByActiveEvent(int eventID);

    @Query("select s from Sponsorship s where s.event.id = ?1")
    List<Sponsorship> findAllByEvent(int eventID);

    @Query("select s from Sponsorship s where s.status = TRUE")
    Collection<Sponsorship> findAllActive();
}
