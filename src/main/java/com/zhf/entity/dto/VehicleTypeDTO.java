package com.zhf.entity.dto;

import com.zhf.constraints.VehicleTypeCheck;
import lombok.Data;

@Data
public class VehicleTypeDTO {

    @VehicleTypeCheck(vehicleTypeValue = {"1", "2"}, message = "车辆类型不正确")
    private String vehicleType;


}
