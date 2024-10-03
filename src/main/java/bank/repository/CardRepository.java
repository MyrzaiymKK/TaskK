package bank.repository;

import bank.entities.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("select c from Card c where c.numberOfCard =:numberOfCard")
    Card findCardWithNum(String numberOfCard);

    @Query("select c from Card c where c.numberOfCard =:numberOfCard")
    Optional<Card> getCardWithNum(String numberOfCard);
}
