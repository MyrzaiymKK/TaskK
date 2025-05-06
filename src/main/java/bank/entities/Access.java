package bank.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "access")
@NoArgsConstructor
public class Access {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_seq")
    @SequenceGenerator(name = "access_seq", allocationSize = 1, initialValue = 1)
    private Long Id;
    private String name;
    private String category;
    private double money;
    private String location;
    private String status;

    public Access(Long id, String name, String category, double money, String location, String status) {
        Id = id;
        this.name = name;
        this.category = category;
        this.money = money;
        this.location = location;
        this.status = status;
    }
}
