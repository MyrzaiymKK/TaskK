package bank.service;

import bank.dto.request.CardRequest;
import bank.dto.request.MoneyRequest;
import bank.dto.request.TransferrRequest;
import bank.dto.response.SimpleResponse;
import bank.entities.Payment;

public interface ProcessingCenterStrategy {

    Payment getPayment();

    SimpleResponse transfer(TransferrRequest transferrRequest);  // Выпуск карты
    SimpleResponse replenishCard(MoneyRequest moneyRequest);  // Пополнение карты
    SimpleResponse debitingFromCard(MoneyRequest moneyRequest);  // Списание с карты
    SimpleResponse reissueCard(CardRequest cardRequest);
    SimpleResponse prolongCard(CardRequest cardRequest);
}
