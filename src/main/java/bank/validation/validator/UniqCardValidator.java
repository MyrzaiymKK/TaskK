package bank.validation.validator;

import bank.repository.CardRepository;
import bank.validation.UniqCardNumValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;

public class UniqCardValidator implements ConstraintValidator<UniqCardNumValidation,String>, Annotation {

    @Autowired
    private CardRepository cardRepo;


    @Override
    public boolean isValid(String cardNum, ConstraintValidatorContext constraintValidatorContext) {
        if (cardNum == null){
            return true;
        }
        return !cardRepo.getCardWithNum(cardNum).isPresent();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
