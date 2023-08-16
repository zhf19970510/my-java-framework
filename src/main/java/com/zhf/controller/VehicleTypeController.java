package com.zhf.controller;


import com.zhf.entity.base.BaseResult;
import com.zhf.entity.dto.VehicleTypeDTO;
import com.zhf.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleTypeController {

    private static Logger logger = LoggerFactory.getLogger(VehicleTypeController.class);

    /**
     * 自定义validation注解测试controller
     *
     * @param vehicleTypeDTO    参数
     * @return
     */
    @PostMapping("/testValidation")
    public BaseResult<String> testValidation(@RequestBody @Validated VehicleTypeDTO vehicleTypeDTO) {
        return BaseResult.success(vehicleTypeDTO.getVehicleType());
    }
}
