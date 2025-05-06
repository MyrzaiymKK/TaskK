package bank.dto.request;

import bank.entities.Limit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepoRequest {
    private String cardNum;
    private String passwordOfCard;
    private double summa;
    private Limit limit;
}
