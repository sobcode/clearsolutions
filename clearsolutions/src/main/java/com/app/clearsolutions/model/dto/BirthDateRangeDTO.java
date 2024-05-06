package com.app.clearsolutions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a range of birth dates.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BirthDateRangeDTO {
    private LocalDate from;
    private LocalDate to;

    /**
     * Constructs a BirthDateRangeDTO object from provided string representations of dates.
     *
     * @param from The start date as a string (yyyy-MM-dd format).
     * @param to   The end date as a string (yyyy-MM-dd format).
     */
    public BirthDateRangeDTO(String from, String to) {
        if(!from.isEmpty()) {
            this.from = LocalDate.parse(from);
        }
        if(!to.isEmpty()) {
            this.to = LocalDate.parse(to);
        }
    }
}
