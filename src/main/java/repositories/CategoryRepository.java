package repositories;

import domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

    @Query("select c.nameEn from Category c")
    Collection<String> getNamesEn();

    @Query("select c.nameEs from Category c")
    Collection<String> getNamesEs();

}
