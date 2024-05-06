package com.app.clearsolutions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class representing pagination information.
 * This class encapsulates the total number of items and the total number of pages for pagination purposes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private long numberOFItems;
    private int numberOfPages;
}
