package repositories;

import domain.Book;
import domain.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("select count(t) from Transaction t where t.book.id = ?1")
    Integer numTransaction(int bookId);

    @Query("select b from Book b where b.reader = ?1 and not exists(select t from Transaction t where t.book = b)")
    Collection<Book> getBooksWithNoTransactionsByReader(Reader r);

    @Query("select b from Book b where b.moment < ?1 and not exists(select t from Transaction t where t.book = b)")
    Collection<Book> findAllInactiveBooks(java.sql.Date date);
}
