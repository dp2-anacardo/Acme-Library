package repositories;

import domain.Reader;
import domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import services.TransactionService;

import java.util.Collection;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("select t from Transaction t where t.isSale = true and t.seller = ?1")
    Collection<Transaction> getSalesByReader(Reader r);

    @Query("select t from Transaction t where t.isSale = true and t.buyer = ?1")
    Collection<Transaction> getBuysByReader(Reader r);

    @Query("select t from Transaction t where t.isSale = false and t.seller = ?1")
    Collection<Transaction> getExchangesByReader(Reader r);

    @Query("select t from Transaction t where t.isSale = true and t.buyer is null")
    Collection<Transaction> getSalesWithoutBuyer();

    @Query("select t from Transaction t where t.isSale = false and not exists(select o from Offer o where o.transaction = t and o.status like 'ACCEPTED')")
    Collection<Transaction> getExchanges();

    @Query("select t from Transaction t join t.complaints c where c.id=?1")
    Transaction getTransactionByComplaint(final int complaintId);

}
