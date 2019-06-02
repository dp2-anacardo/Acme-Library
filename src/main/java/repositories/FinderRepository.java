package repositories;

import domain.Finder;
import domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

    @Query("select t from Transaction t where (t.book.title like %?1% or t.book.author like %?1% " +
            "or t.book.publisher like %?1% or t.book.description like %?1%) and t.isFinished=FALSE")
    Collection<Transaction> getTransactionsByKeyWord(String keyWord);

    @Query("select t from Transaction t where (t.book.languageB like ?1 or t.book.isbn like ?1) and t.isFinished=FALSE")
    Collection<Transaction> getTransactionsContainsKeyWord(String keyWord);

    @Query("select t from Transaction t join t.book.categories c where (c.nameEs like ?1 or c.nameEn like ?1) " +
            "and t.isFinished=FALSE")
    Collection<Transaction> getTransactionsByCategory(String category);

    @Query("select t from Transaction t where t.book.status like ?1 and t.isFinished=FALSE")
    Collection<Transaction> getTransactionsByStatus(String status);

    @Query("select f from Finder f join f.transactions t where t.id=?1")
    Collection<Finder> findAllByTransaction(int transactionId);
}
