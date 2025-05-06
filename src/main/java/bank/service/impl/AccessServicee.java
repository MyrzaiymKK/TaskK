package bank.service.impl;

import bank.dto.request.AccessReq;
import bank.dto.request.AccesssRequest;
import bank.dto.response.AcceessRES;
import bank.dto.response.SimpleResponse;
import bank.entities.Access;
import bank.repository.AccesssRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessServicee {

    private final AccesssRepo accesssRepo;

    public SimpleResponse addAccess(AccesssRequest accesssRequest){
        Access access = new Access();
        access.setName(accesssRequest.getName());
        access.setCategory(accesssRequest.getCategory());
        access.setMoney(accesssRequest.getMoney());
        access.setLocation(accesssRequest.getLocation());
        access.setStatus(accesssRequest.getStatus());
        accesssRepo.save(access);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved!")
                .build();
    }

    public AcceessRES search(AccessReq accessReq){

        Access newAccess =  accesssRepo.searchWithNum(accessReq.getName());
        return AcceessRES.builder()
                .name(newAccess.getName())
                .category(newAccess.getCategory())
                .money(newAccess.getMoney())
                .location(newAccess.getLocation())
                .status(newAccess.getStatus())
                .build();
    }
}
