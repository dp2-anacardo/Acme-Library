package repositories;

import domain.Finder;
import domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

    @Query("select b from Book b where (b.title like %?1% or b.author like %?1% or b.publisher like %?1% or b.description like %?1%)")
    Collection<Book> getBooksByKeyWord(String keyWord);

    @Query("select b from Book b where (b.languaje like ?1 or b.isbn like ?1)")
    Collection<Book> getBooksContainsKeyWord(String keyWord);

    @Query("select b from Book b join b.categories c where c.nameEs like ?1 or c.nameEn like ?1")
    Collection<Book> getBooksByCategory(String category);

    @Query("select b from Book b where b.status like ?1")
    Collection<Book> getBooksByStatus(String status);

    @Query("select f from Finder f join f.books b where b.id=?1")
    Collection<Finder> findAllByBook(int booksId);
}
