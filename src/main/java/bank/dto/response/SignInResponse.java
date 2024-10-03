package bank.dto.response;

import bank.entities.Payment;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    String token;
    String clientFirstName;
    String clientLastName;
    String numberOfCard;
    Payment paymentSystem;
    double balance;
}
