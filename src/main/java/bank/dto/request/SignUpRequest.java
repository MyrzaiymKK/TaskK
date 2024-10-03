package bank.dto.request;

import bank.entities.Payment;
import bank.validation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
  private String clientFirstName;
  private String clientLastName;
  @PhoneNumberValidation

  private String phoneNumber;
  @UniqCardNumValidation
  private String numberOfCard;
  @PasswordValidation
  private String passwordOfCard;
  private Payment payment;
  private double balance;
}
