package repositories;

import domain.Book;
import domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

    //TODO: Probar en ejecución
    @Query("select b from Book b join b.categories c where ?1 member of c")
    Collection<Book> getBooksWithCategoryParam(Category category);

    @Query("select c from Category c where c.nameEs like 'Por defecto'")
    Category getDefaultCategory();
}
