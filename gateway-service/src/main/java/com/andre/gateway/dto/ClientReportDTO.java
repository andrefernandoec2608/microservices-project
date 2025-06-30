package com.andre.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientReportDTO {
    private String name;
    private List<AccountDTO> accounts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountDTO {
        private String accountNumber;
        private String accountType;
    }
}
