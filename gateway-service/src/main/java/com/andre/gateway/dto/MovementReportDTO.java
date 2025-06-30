package com.andre.gateway.dto;

import java.util.List;

public record MovementReportDTO(
        String clientName,
        String accountType,
        List<MovementInfo> movements
) {
    public record MovementInfo(
            String date,
            String movementType,
            Double amount,
            Double balance
    ) {}
}