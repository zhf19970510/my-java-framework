package com.zhf.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class VehicleTypeCheckValidator implements ConstraintValidator<VehicleTypeCheck, String> {

    private List<String> vehicleTypeCheckValue = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(vehicleTypeCheckValue.contains(value)) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize(VehicleTypeCheck constraintAnnotation) {
        vehicleTypeCheckValue = Arrays.asList(constraintAnnotation.vehicleTypeValue());
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
