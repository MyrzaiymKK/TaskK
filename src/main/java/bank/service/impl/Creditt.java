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
public class Creditt {

    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    public SimpleResponse creditt(DepoRequest depoRequest) {
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
                multiplier = 0.01;
                break;
            case YEAR7:
                multiplier = 0.005;
                break;
            case YEAR10:
                multiplier = 0.004;
                break;
            case YEAR12:
                multiplier = 0.003;
                break;
            case YEAR15:
                multiplier = 0.002;
                break;
            default:
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Invalid limit type: " + depoRequest.getLimit())
                        .build();
        }


        double sum;
        switch (depoRequest.getLimit()) {
            case YEAR5:
                sum = 30;
                break;
            case YEAR7:
                sum = 45;
                break;
            case YEAR10:
                sum = 60;
                break;
            case YEAR12:
                sum = 90;
                break;
            case YEAR15:
                sum = 120;
                break;
            default:
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Invalid limit type: " + depoRequest.getLimit())
                        .build();
        }

        double balance = (depoRequest.getSumma() * multiplier);
        double newBalance = (depoRequest.getSumma() + balance * sum);
        card.setBalance(newBalance);
        cardRepository.save(card);

        double money = card.getBalance() / sum ;

        // Формирование успешного ответа
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Successfully! Summa: %.2f  ||   Prosent: %.3f " +
                        "                                 Summa after : %.2f  || Ror day:  %.2f    ||  DAY: %.2f" , depoRequest.getSumma(), multiplier,card.getBalance(), money, sum))
                .build();
    }

    public SimpleResponse dep(DepoRequest depoRequest) {
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
                multiplier = 0.08;
                break;
            case YEAR7:
                multiplier = 0.07;
                break;
            case YEAR10:
                multiplier = 0.06;
                break;
            case YEAR12:
                multiplier = 0.06;
                break;
            case YEAR15:
                multiplier = 0.06;
                break;
            default:
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Invalid limit type: " + depoRequest.getLimit())
                        .build();
        }


        double sum;
        switch (depoRequest.getLimit()) {
            case YEAR5:
                sum = 3;
                break;
            case YEAR7:
                sum = 4;
                break;
            case YEAR10:
                sum = 5;
                break;
            case YEAR12:
                sum = 6;
                break;
            case YEAR15:
                sum = 7;
                break;
            default:
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Invalid limit type: " + depoRequest.getLimit())
                        .build();
        }

        double balance = (depoRequest.getSumma() * multiplier);
        double newBalance = (depoRequest.getSumma() + balance * sum);
        card.setBalance(newBalance);
        cardRepository.save(card);

//        double money = card.getBalance() / sum ;

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(String.format("Successfully! Summa: %.2f  ||   Prosent: %.2f " +
                        "                                 Summa after : %.2f   ||    Limit:  %.2f   year" , depoRequest.getSumma(), multiplier,card.getBalance(), sum))
                .build();
    }


}
