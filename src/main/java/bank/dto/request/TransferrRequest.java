package bank.dto.request;

import bank.entities.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferrRequest {
    private String cardNum;
    private String otherCardNum;
    private String passwordOfCard;
    private double summa;
    private Payment payment;

    public TransferrRequest(String cardNum, String otherCardNum, String passwordOfCard, double summa) {
        this.cardNum = cardNum;
        this.otherCardNum = otherCardNum;
        this.passwordOfCard = passwordOfCard;
        this.summa = summa;
    }
}
