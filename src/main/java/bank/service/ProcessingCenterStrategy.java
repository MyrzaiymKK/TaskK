package bank.service;

import bank.dto.request.MoneyRequest;
import bank.dto.request.TransferrRequest;
import bank.dto.response.SimpleResponse;
import bank.entities.Card;
import org.springframework.transaction.annotation.Transactional;

public interface ProcessingCenterStrategy {
    SimpleResponse transfer(TransferrRequest transferrRequest);  // Выпуск карты
    SimpleResponse replenishCard(MoneyRequest moneyRequest);  // Пополнение карты
    SimpleResponse debitingFromCard(MoneyRequest moneyRequest);  // Списание с карты
    SimpleResponse reissueCard(CardRequest cardRequest);
    SimpleResponse prolongCard(CardRequest cardRequest);
}
