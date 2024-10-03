package bank.api;

import bank.dto.request.MoneyRequest;
import bank.dto.request.TransferrRequest;
import bank.dto.response.SimpleResponse;
import bank.service.CardRequest;
import bank.service.impl.MasterCardProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/master")
public class MasterCardApi {

    private final MasterCardProcessingService masterCardProcessing;


    @PostMapping("/replenishMaster")
    public SimpleResponse replenishVisa(@RequestBody MoneyRequest moneyRequest){
        return masterCardProcessing.replenishCard(moneyRequest);
    }

    @PostMapping("/debitingMaster")
    public SimpleResponse debitingVisa(@RequestBody MoneyRequest moneyRequest){
        return masterCardProcessing.debitingFromCard(moneyRequest);
    }

    @PostMapping("/transferMaster")
    public SimpleResponse transfer(@RequestBody TransferrRequest transferrRequest){
        return masterCardProcessing.transfer(transferrRequest);
    }

    @PostMapping("/reissueCard")
    public SimpleResponse reissueCard(@RequestBody CardRequest cardRequest){
        return masterCardProcessing.reissueCard(cardRequest);
    }

    @PostMapping("/prolongCard")
    public SimpleResponse prolongCard(@RequestBody CardRequest cardRequest){
        return masterCardProcessing.prolongCard(cardRequest);
    }

}
