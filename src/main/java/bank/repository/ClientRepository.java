package bank.repository;

import bank.entities.Client;
import bank.exeption.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository  extends JpaRepository<Client,Long> {

    @Query("select c from Client c where c.clientFirstName =:firstName")
    Optional<Client> findByFirstName(String firstName);


    default Client getByFirstName(String firstName){
        return findByFirstName(firstName).orElseThrow(() ->
                new NotFoundException("User with this name: " + firstName + " not found"));
    }

    @Query("SELECT c FROM Client c JOIN c.clientCardList cr WHERE cr.numberOfCard = :num")
    Client findClientWithNum( String num);

    @Query("select c from Client c  where  c.phoneNumber =:phoneNumber")
    Client getClientWithPhone(String phoneNumber);

    @Query("select c from Client c  where  c.phoneNumber =:phoneNumber")
    Optional<Client> findClientWithPhone(String phoneNumber);
}
