package repositories;

import domain.MessageBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MessageBoxRepository extends JpaRepository<MessageBox, Integer> {

    @Query("select a.boxes from Actor a where a.id = ?1")
    Collection<MessageBox> findAllByActor(int actorID);

}