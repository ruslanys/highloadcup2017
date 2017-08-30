package me.ruslanys.highloadcup.dto.response;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class AvgResponseDto {

    @NonNull
    private BigDecimal avg;

    public AvgResponseDto(BigDecimal avg) {
        this.avg = (avg != null ? avg.setScale(5, BigDecimal.ROUND_HALF_UP) : new BigDecimal("0.0"));
    }

    @Override
    public String toString() {
        return "{\"avg\":" + avg.toString() + "}";
    }

}
