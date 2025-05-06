package bank.service.impl;

import bank.dto.request.MoneyRequest;
import bank.dto.request.TransferrRequest;
import bank.dto.response.SimpleResponse;
import bank.entities.Card;
import bank.entities.Payment;
import bank.repository.CardRepository;
import bank.dto.request.CardRequest;
import bank.service.ProcessingCenterStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VisaProcessingService implements ProcessingCenterStrategy {
    private final PasswordEncoder passwordEncoder;
    private final CardRepository cardRepository;

    @Override
    public Payment getPayment() {
        return Payment.VISA;
    }

    //TODO Этот метод реализует перевод средств между двумя картами.
    // Для каждой транзакции проверяется, совпадают ли пароли карт,
    // и достаточно ли средств для перевода.
    // Если система двуз карт одинаковый то идет без % перевод
    // иначе идет с %
    @Override
    public SimpleResponse transfer(TransferrRequest transferrRequest) {
        Card card = cardRepository.findCardWithNum(transferrRequest.getCardNum());
        Card foundCard = cardRepository.findCardWithNum(transferrRequest.getOtherCardNum());

        String encodePassword = card.getPasswordOfCard();
        String password = transferrRequest.getPasswordOfCard();

        boolean matches = passwordEncoder.matches(password, encodePassword);
        if (!matches) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Invalid password")
                    .build();
        }

        if (card.getPayment().equals(foundCard.getPayment())) {
            if (card.getBalance() >= transferrRequest.getSumma()) {
                card.setBalance(card.getBalance() - transferrRequest.getSumma());
                foundCard.setBalance(foundCard.getBalance() + transferrRequest.getSumma());
                cardRepository.save(card);
                cardRepository.save(foundCard);
            } else {
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Insufficient funds")
                        .build();
            }
        } else {
            if (card.getBalance() >= transferrRequest.getSumma() * 1.02) {
                double commission = transferrRequest.getSumma() * 0.02;
                card.setBalance(card.getBalance() - transferrRequest.getSumma() - commission);
                foundCard.setBalance(foundCard.getBalance() + transferrRequest.getSumma());
                cardRepository.save(card);
                cardRepository.save(foundCard);
            } else {
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Insufficient funds for transfer and commission")
                        .build();
            }
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully transferred!")
                .build();
    }


    // TODO Этот метод реализует пополнение баланса карты.
    @Override
    public SimpleResponse replenishCard(MoneyRequest moneyRequest) {
        Card card = cardRepository.findCardWithNum(moneyRequest.getCardNum());

        boolean matches = passwordEncoder.matches(moneyRequest.getPasswordOfCard(), card.getPasswordOfCard());
        if (!matches) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Incorrect password for card " + card.getNumberOfCard())
                    .build();
        }

        // Добавление суммы к балансу
        card.setBalance(card.getBalance() + moneyRequest.getSumma());
        cardRepository.save(card);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully replenished " + card.getPayment() + " balance: " + card.getBalance())
                .build();
    }


    // TODO Этот метод реализует списание средств с карты.
    @Override
    public SimpleResponse debitingFromCard(MoneyRequest moneyRequest) {
        Card card = cardRepository.findCardWithNum(moneyRequest.getCardNum());

        boolean matches = passwordEncoder.matches(moneyRequest.getPasswordOfCard(), card.getPasswordOfCard());
        if (!matches) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Incorrect password!")
                    .build();
        }
        if (card.getBalance() >= moneyRequest.getSumma()) {
            card.setBalance(card.getBalance() - moneyRequest.getSumma());
            cardRepository.save(card);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully withdrawn from " + card.getPayment() + " balance: " + card.getBalance())
                    .build();
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Not enough funds on " + card.getPayment() + " balance: " + card.getBalance())
                .build();
    }

    //TODO Перевыпуск карты.
    // Проверяет истечение срока действия карты и производит выпуск новой карты.
    @Override
    public SimpleResponse reissueCard(CardRequest cardRequest) {
        Card card = cardRepository.findCardWithNum(cardRequest.getCardNum());

        if (card.getExpiryDate().isAfter(LocalDate.now())) {
            card.setExpiryDate(LocalDate.now().plusYears(3));
            cardRepository.save(card);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Card reissued successfully. New expiry date: " + card.getExpiryDate())
                    .build();
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Card is still valid, no need to reissue.")
                .build();
    }


    //TODO Пролонгация карты. Продлевает срок действия карты, если она ещё не истекла.
    @Override
    public SimpleResponse prolongCard(CardRequest cardRequest) {
        Card card = cardRepository.findCardWithNum(cardRequest.getCardNum());

        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            card.setExpiryDate(card.getExpiryDate().plusYears(1));
            cardRepository.save(card);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Card prolonged successfully. New expiry date: " + card.getExpiryDate())
                    .build();
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Card cannot be prolonged, already expired.")
                .build();
    }


}
