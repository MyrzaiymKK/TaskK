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
public class Depozite {


    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;


    public SimpleResponse replenishDepo(DepoRequest depoRequest) {
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
            case WEEK:
                multiplier = 0.01;
                break;
            case MONTH:
                multiplier = 0.02;
                break;
            case QUARTER:
                multiplier = 0.03;
                break;
            case YEAR:
                multiplier = 0.04;
                break;
            default:
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Invalid limit type: " + depoRequest.getLimit())
                        .build();
        }

        // Обновление баланса карты
        double newBalance = (depoRequest.getSumma() + depoRequest.getSumma() * multiplier);
       double balance =  card.getBalance() + newBalance;
        card.setBalance(balance);
        cardRepository.save(card);

//        double balance = depoRequest.getSumma() + depoRequest.getSumma() * multiplier;

        // Формирование успешного ответа
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Successfully replenished. Current balance: %.2f %s", card.getBalance(), card.getPayment()))
                .build();
}

}
