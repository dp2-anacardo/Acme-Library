package repositories;

import domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Integer> {

    @Query("select o from Offer o where o.reader.id = ?1")
    Collection<Offer> getAllByReader(final int readerId);
}
