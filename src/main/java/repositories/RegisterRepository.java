package repositories;

import domain.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {

    @Query("select r from Register r where r.reader = ?1")
    Collection<Register> getRegistersByReader(int readerId);
}
