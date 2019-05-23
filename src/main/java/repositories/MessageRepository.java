package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("select mb.messages from MessageBox mb where mb.id = ?1")
    Collection<Message> findByMessageBox(int messageBoxID);

    @Query("select m from Message m where m.sender.id=?1")
    Collection<Message> findAllSentByActor(int actorID);

    @Query("select (count(s))/(select (count(t)*1.0) from Message t where t.sender.id=?1) from Message s where s.sender.id=?1 and s.isSpam=TRUE")
    Double findSpamRatioByActor(int actorID);

    @Query("select m from Message m join m.recipients r where r.id=?1")
    Collection<Message> findAllReceivedByActor(int actorID);

}