package com.test.modusbox.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.test.modusbox.model.entities.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private long id;
    private String transactionId;
    Supplier supplier;
    private LocalDate fecha;
    private LocalTime hora;
    private String content;
}
