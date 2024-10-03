package bank.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyRequest {
    private String cardNum;
    private String passwordOfCard;
    private double summa;

    public MoneyRequest(String cardNum, String passwordOfCard, double summa) {
        this.cardNum = cardNum;
        this.passwordOfCard = passwordOfCard;
        this.summa = summa;
    }
}
