package bank.service.impl;


import bank.dto.request.DepoRequest;
import bank.dto.response.SimpleResponse;
import bank.entities.Card;
import bank.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class Credit {

    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;


    public SimpleResponse credit(DepoRequest depoRequest) {
        Card card = cardRepository.findCardWithNum(depoRequest.getCardNum());

        boolean matches = passwordEncoder.matches(depoRequest.getPasswordOfCard(), card.getPasswordOfCard());
        if (!matches) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Incorrect password for card " + card.getNumberOfCard())
                    .build();
        }
        double multiplier;
        switch (depoRequest.getLimit()) {
            case YEAR5:
                multiplier = 0.3;
                break;
            case YEAR7:
                multiplier = 0.4;
                break;
            case YEAR10:
                multiplier = 0.5;
                break;
            case YEAR12:
                multiplier = 0.6;
                break;
            case YEAR15:
                multiplier = 0.7;
                break;
            default:
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Invalid limit type: " + depoRequest.getLimit())
                        .build();
        }


        double newBalance = (depoRequest.getSumma() + depoRequest.getSumma() * multiplier);
        card.setBalance(newBalance);
        cardRepository.save(card);
        double sum;
        switch (depoRequest.getLimit()) {
            case YEAR5:
                sum = 5;
                break;
            case YEAR7:
                sum = 7;
                break;
            case YEAR10:
                sum = 10;
                break;
            case YEAR12:
                sum = 12;
                break;
            case YEAR15:
                sum = 13;
                break;
            default:
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Invalid limit type: " + depoRequest.getLimit())
                        .build();
        }

        double money = card.getBalance() / sum / 12;


        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Successfully! Summa: %.2f  ||   Prosent: %.2f " +
                        "                                 Summa after : %.2f  || Ror month:  %.2f" , depoRequest.getSumma(), multiplier,card.getBalance(), money))
                .build();
    }


}
