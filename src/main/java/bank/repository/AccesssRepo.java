package bank.repository;

import bank.entities.Access;
import bank.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccesssRepo extends JpaRepository<Access, Long> {

    @Query("select a from Access a where a.name =:name")
    Access searchWithNum(String name);

}
