package repositories;

import domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    //TODO: probar query
    @Query("select count(t) from Transaction t where t.book.id = ?1")
    Integer numTransaction(int bookId);
}
