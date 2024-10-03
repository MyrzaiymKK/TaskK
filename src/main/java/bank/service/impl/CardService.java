package bank.service.impl;

import bank.dto.request.SignInCardRequest;
import bank.dto.response.SignInResponse;
import bank.dto.request.SignUpRequest;
import bank.dto.response.SimpleResponse;
import bank.entities.Card;
import bank.entities.Client;
import bank.entities.Payment;
import bank.repository.CardRepository;
import bank.repository.ClientRepository;
import bank.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final PasswordEncoder passwordEncoder;
    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;
    private final JwtService jwtService;

    public static String generateNum(Payment paymentSystem) {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        cardNumber.append(paymentSystem.getPrefix());

        for (int i = 0; i < 12; i++) {
            cardNumber.append(random.nextInt(10));
        }

        return cardNumber.toString();
    }


    public SignInResponse singInToCard(SignInCardRequest signInCardRequest) {
            Client client = clientRepository.findClientWithNum(signInCardRequest.getNumberOfCard());
        if (client == null) {
            throw new RuntimeException("Client not found");
        }
        Card card = cardRepository.findCardWithNum(signInCardRequest.getNumberOfCard());
        if (card == null) {
            throw new RuntimeException("Card not found");
        }

        Client client1 = card.getClient();

        String encodePassword = card.getPasswordOfCard();
        String password = signInCardRequest.getPasswordOfCard();

        boolean matches = passwordEncoder.matches(password, encodePassword);

        if (!matches) throw new RuntimeException("Invalid password");

        String token = jwtService.createToken(client1);

        for (Card card1 : client1.getClientCardList()) {
            if (card1.getNumberOfCard().equals(signInCardRequest.getNumberOfCard())) {
                return SignInResponse.builder()
                        .token(token)
                        .clientFirstName(client1.getClientFirstName())
                        .clientLastName(client1.getClientLastName())
                        .numberOfCard(card1.getNumberOfCard())
                        .balance(card1.getBalance())
                        .paymentSystem(card1.getPayment())
                        .build();
            }
        }
        return null;


    }


    public SimpleResponse createUser(SignUpRequest signUpRequest) {
        Client client = clientRepository.getClientWithPhone(signUpRequest.getPhoneNumber());

        if (client == null) {
            client = new Client();
            client.setClientFirstName(signUpRequest.getClientFirstName());
            client.setClientLastName(signUpRequest.getClientLastName());
            client.setPhoneNumber(signUpRequest.getPhoneNumber());
            clientRepository.save(client);

            boolean cardExists = client.getClientCardList().stream()
                    .anyMatch(card -> card.getPayment().equals(signUpRequest.getPayment()));

            if (cardExists) {
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("A card with this type already exists for this client!")
                        .build();
            } else {
                Card card = new Card();
                card.setNumberOfCard(generateNum(signUpRequest.getPayment()));
                card.setPasswordOfCard(passwordEncoder.encode(signUpRequest.getPasswordOfCard()));
                card.setExpiryDate(LocalDate.now());
                card.setPayment(signUpRequest.getPayment());
                card.setClient(client);

                cardRepository.save(card);
                client.getClientCardList().add(card);
                clientRepository.save(client);
            }
        } else if (client != null) {

            boolean cardExists = client.getClientCardList().stream()
                    .anyMatch(card -> card.getPayment().equals(signUpRequest.getPayment()));

            if (cardExists) {
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("A card with this type already exists for this client!")
                        .build();
            }

            Card card = new Card();
            card.setNumberOfCard(generateNum(signUpRequest.getPayment()));
            card.setPasswordOfCard(passwordEncoder.encode(signUpRequest.getPasswordOfCard()));
            card.setBalance(signUpRequest.getBalance());
            card.setExpiryDate(LocalDate.now());
            card.setPayment(signUpRequest.getPayment());

            card.setClient(client);
            cardRepository.save(card);
            client.getClientCardList().add(card);
            clientRepository.save(client);
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully created!")
                .build();
    }

}
