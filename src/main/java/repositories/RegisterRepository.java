package repositories;

import domain.Event;
import domain.Reader;
import domain.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {

    @Query("select r from Register r where r.reader.id = ?1")
    Collection<Register> getRegistersPerReader(int readerId);

    @Query("select c from Event c where ?1 member of c.registers")
    Collection<Event> getEventPerReader(Register r);

    @Query("select e from Event e join e.registers r where r.reader = ?1")
    Collection<Event> getEventsPerReader(Reader reader);

}
