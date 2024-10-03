package bank.entities;


import lombok.Getter;

@Getter
public enum Payment {
    VISA("4169"),
    MASTERCARD("3757");

    private final String prefix;

    Payment(String prefix) {
        this.prefix = prefix;
    }

}
