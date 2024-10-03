package bank.api;

import bank.dto.request.SignInCardRequest;
import bank.dto.response.SignInResponse;
import bank.dto.request.SignUpRequest;
import bank.dto.response.SimpleResponse;
import bank.service.impl.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthApi {

    private final CardService cardService;

    @PostMapping("/signUp")
    SimpleResponse signUps (@Valid  @RequestBody SignUpRequest signUpRequest){
        return cardService.createUser(signUpRequest);
    }


    @PostMapping("/signIn")
    SignInResponse signIn(@Valid @RequestBody SignInCardRequest signInCardRequest){
       return cardService.singInToCard(signInCardRequest);
    }

}
