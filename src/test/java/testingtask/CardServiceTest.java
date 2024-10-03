//package testingtask;
//
//import bank.dto.request.MoneyRequest;
//import bank.dto.request.TransferrRequest;
//import bank.dto.response.SimpleResponse;
//import bank.entities.Card;
//import bank.entities.Payment;
//import bank.repository.CardRepository;
//import bank.service.impl.VisaProcessingService;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//
//@RequiredArgsConstructor
//@SpringBootTest
//public class CardServiceTest {
//
//    @Mock
//    private CardRepository cardRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private VisaProcessingService visaProcessing;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testTransferSuccessSamePaymentSystem() {
//        // Arrange
//        Card senderCard = new Card(1L,"123", "password123", 1000.0, Payment.VISA);
//        Card receiverCard = new Card(2L,"456", "password456", 500.0, Payment.VISA); // Изменить Payment на VISA
//
//        TransferrRequest request = new TransferrRequest("123", "456", "password123", 200.0); // Исправлено
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(senderCard);
//        when(cardRepository.findCardWithNum("456")).thenReturn(receiverCard);
//        when(passwordEncoder.matches("password123", senderCard.getPasswordOfCard())).thenReturn(true);
//
//        // Act
//        SimpleResponse response = visaProcessing.transfer(request);
//
//        // Assert
//        assertEquals(800.0, senderCard.getBalance());
//        assertEquals(700.0, receiverCard.getBalance());
//        assertEquals("Successfully transferred!", response.getMessage());
//        assertEquals(HttpStatus.OK, response.getHttpStatus());
//    }
//
//    @Test
//    void testTransferInsufficientFunds() {
//        // Arrange
//        Card senderCard = new Card(1L,"123", "password123", 100.0, Payment.VISA); // Установите баланс ниже запрашиваемой суммы
//        Card receiverCard = new Card(2L,"456", "password456", 500.0, Payment.MASTERCARD);
//
//        TransferrRequest request = new TransferrRequest("123", "456", "password123", 200.0); // Исправлено
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(senderCard);
//        when(cardRepository.findCardWithNum("456")).thenReturn(receiverCard);
//        when(passwordEncoder.matches("password123", senderCard.getPasswordOfCard())).thenReturn(true);
//
//        // Act
//        SimpleResponse response = visaProcessing.transfer(request);
//
//        // Assert
//        assertEquals("Insufficient funds", response.getMessage());
//        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
//    }
//
//    @Test
//    void testTransferWithCommission() {
//        // Arrange
//        Card senderCard = new Card(1L,"123", "password123", 1000.0, Payment.VISA);
//        Card receiverCard = new Card(2L,"456", "password456", 500.0, Payment.MASTERCARD);
//
//        TransferrRequest request = new TransferrRequest("123", "456", "password123", 200.0);
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(senderCard);
//        when(cardRepository.findCardWithNum("456")).thenReturn(receiverCard);
//        when(passwordEncoder.matches("password123", senderCard.getPasswordOfCard())).thenReturn(true);
//
//        // Act
//        SimpleResponse response = visaProcessing.transfer(request);
//
//        // Assert
//        double expectedSenderBalance = 1000.0 - 200.0 - 200.0 * 0.02;
//        assertEquals(expectedSenderBalance, senderCard.getBalance());
//        assertEquals(700.0, receiverCard.getBalance());
//        assertEquals("Successfully transferred!", response.getMessage());
//        assertEquals(HttpStatus.OK, response.getHttpStatus());
//    }
//
//    @Test
//    void testTransferInvalidPassword() {
//        // Arrange
//        Card senderCard = new Card(1L,"123", "password123", 1000.0, Payment.VISA);
//        Card receiverCard = new Card(2L,"456", "password456", 500.0, Payment.MASTERCARD);
//
//        TransferrRequest request = new TransferrRequest("123", "456", "wrongPassword", 200.0);
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(senderCard);
//        when(passwordEncoder.matches("wrongPassword", senderCard.getPasswordOfCard())).thenReturn(false);
//
//        // Act
//        SimpleResponse response = visaProcessing.transfer(request);
//
//        // Assert
//        assertEquals("Invalid password", response.getMessage());
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getHttpStatus());
//    }
//
//    @Test
//    void testReplenishCardSuccess() {
//        // Arrange
//        Card card = new Card(1L,"123", "password123", 1000.0, Payment.VISA);
//        MoneyRequest request = new MoneyRequest("123", "password123", 500.0);
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(card);
//        when(passwordEncoder.matches("password123", card.getPasswordOfCard())).thenReturn(true);
//
//        // Act
//        SimpleResponse response = visaProcessing.replenishCard(request);
//
//        // Assert
//        assertEquals(1500.0, card.getBalance());
//        assertEquals("Successfully replenished VISA balance: 1500.0", response.getMessage());
//        assertEquals(HttpStatus.OK, response.getHttpStatus());
//    }
//
//    @Test
//    void testReplenishCardInvalidPassword() {
//        // Arrange
//        Card card = new Card(1L,"123", "password123", 1000.0, Payment.VISA);
//
//        MoneyRequest request = new MoneyRequest("123", "wrongPassword", 500.0);
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(card);
//        when(passwordEncoder.matches("wrongPassword", card.getPasswordOfCard())).thenReturn(false);
//
//        // Act
//        SimpleResponse response = visaProcessing.replenishCard(request);
//
//        // Assert
//        assertEquals("Incorrect password for card 123", response.getMessage());
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getHttpStatus());
//    }
//
//    @Test
//    void testDebitingFromCardSuccess() {
//        // Arrange
//        Card card = new Card(1L,"123", "password123", 1000.0, Payment.VISA);
//
//        MoneyRequest request = new MoneyRequest("123", "password123", 200.0);
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(card);
//        when(passwordEncoder.matches("password123", card.getPasswordOfCard())).thenReturn(true);
//
//        // Act
//        SimpleResponse response = visaProcessing.debitingFromCard(request);
//
//        // Assert
//        assertEquals(800.0, card.getBalance());
//        assertEquals("Successfully withdrawn from VISA balance: 800.0", response.getMessage());
//        assertEquals(HttpStatus.OK, response.getHttpStatus());
//    }
//
//    @Test
//    void testDebitingFromCardInsufficientFunds() {
//        // Arrange
//        Card card = new Card(1L,"123", "password123", 100.0, Payment.VISA); // Установите баланс ниже запрашиваемой суммы
//
//        MoneyRequest request = new MoneyRequest("123", "password123", 200.0);
//
//        when(cardRepository.findCardWithNum("123")).thenReturn(card);
//        when(passwordEncoder.matches("password123", card.getPasswordOfCard())).thenReturn(true);
//
//        // Act
//        SimpleResponse response = visaProcessing.debitingFromCard(request);
//
//        // Assert
//        assertEquals("Not enough funds on VISA balance: 100.0", response.getMessage());
//        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
//    }
//}
