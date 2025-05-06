package bank.api;

import bank.dto.request.*;
import bank.dto.response.AcceessRES;
import bank.dto.response.SimpleResponse;
import bank.entities.Access;
import bank.service.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/processing")
public class ProcessingApi {

    private final VisaProcessingService visaProcessing;
    private final MasterCardProcessingService masterCardProcessing;
    private final CardService cardService;
    private final Depozite depozite;


    private final AccessServicee accessSer;

    @PostMapping("/replenishVisa")
    public SimpleResponse replenishVisa(@RequestBody MoneyRequest moneyRequest){
        return visaProcessing.replenishCard(moneyRequest);
    }

    @PostMapping("/debitingVisa")
    public SimpleResponse debitingVisa(@RequestBody MoneyRequest moneyRequest){
        return visaProcessing.debitingFromCard(moneyRequest);
    }

//    @PostMapping("/transferVisa")
//    public SimpleResponse transfer(@RequestBody TransferrRequest transferrRequest){
//        return cardService.processing(transferrRequest);
//    }

    @PostMapping("/reissueCard")
    public SimpleResponse reissueCard(@RequestBody CardRequest cardRequest){
        return visaProcessing.reissueCard(cardRequest);
    }

    @PostMapping("/prolongCard")
    public SimpleResponse prolongCard(@RequestBody CardRequest cardRequest){
        return visaProcessing.prolongCard(cardRequest);
    }

    @PostMapping("/depoCard")
    public SimpleResponse depoziteCard(@RequestBody DepoRequest depoRequest){
        return depozite.replenishDepo(depoRequest);
    }





    @PostMapping("/access")
    public SimpleResponse dep(@RequestBody AccesssRequest accesssRequest){
        return accessSer.addAccess(accesssRequest);
    }

    @PostMapping("/accessSearch")
    AcceessRES acc(@RequestBody AccessReq accesssReq){
        return accessSer.search(accesssReq);
    }

}
