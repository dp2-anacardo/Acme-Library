package repositories;

import domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>{

    @Query("select e from Event e where e.isFinal = TRUE")
    Collection<Event> findAllInFinal();

    @Query("select e from Event e where e.isFinal = true and CURRENT_DATE < e.date")
    Collection<Event> getFutureEventsFinal();

    @Query("select e from Event e where e.organizer.id = ?1")
    Collection<Event> getEventsPerOrOrganizer(int organizerId);
}
