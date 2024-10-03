package bank.api;


import bank.dto.request.MoneyRequest;
import bank.dto.request.TransferrRequest;
import bank.dto.response.SimpleResponse;
import bank.service.CardRequest;
import bank.service.impl.VisaProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/visa")
public class VisaApi {

    private final VisaProcessingService visaProcessing;



    @PostMapping("/replenishVisa")
    public SimpleResponse replenishVisa(@RequestBody MoneyRequest moneyRequest){
        return visaProcessing.replenishCard(moneyRequest);
    }

    @PostMapping("/debitingVisa")
    public SimpleResponse debitingVisa(@RequestBody MoneyRequest moneyRequest){
        return visaProcessing.debitingFromCard(moneyRequest);
    }

    @PostMapping("/transferVisa")
    public SimpleResponse transfer(@RequestBody TransferrRequest transferrRequest){
        return visaProcessing.transfer(transferrRequest);
    }

    @PostMapping("/reissueCard")
    public SimpleResponse reissueCard(@RequestBody CardRequest cardRequest){
        return visaProcessing.reissueCard(cardRequest);
    }

    @PostMapping("/prolongCard")
    public SimpleResponse prolongCard(@RequestBody CardRequest cardRequest){
        return visaProcessing.prolongCard(cardRequest);
    }

}
