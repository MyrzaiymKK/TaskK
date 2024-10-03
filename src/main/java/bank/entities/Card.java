package bank.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    @SequenceGenerator(name = "card_seq", allocationSize = 1, initialValue = 1)
    private Long Id;
    private String numberOfCard;
    private String passwordOfCard;
    private double balance;
    private LocalDate expiryDate;
    @Enumerated(EnumType.STRING)
    private Payment payment;

    @ManyToOne
    private Client client;

    public Card(Long id, String numberOfCard, String passwordOfCard, double balance, Payment payment) {
        Id = id;
        this.numberOfCard = numberOfCard;
        this.passwordOfCard = passwordOfCard;
        this.balance = balance;
        this.payment = payment;

    }
}
