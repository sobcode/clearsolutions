package com.app.clearsolutions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing data along with pagination information.
 * This DTO facilitates constructing responses with data and pagination information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataDTO {
    private List<Object> data;
    private Pagination pagination;

    {
        data = new ArrayList<>();
    }

    /**
     * Constructs a DataDTO object with the provided pagination and data objects.
     *
     * @param pagination The pagination information.
     * @param objects    The data objects.
     */
    public DataDTO(Pagination pagination, List<Object> objects) {
        this.pagination = pagination;
        data.addAll(objects);
    }

    /**
     * Constructs a DataDTO object with the provided data object.
     *
     * @param object The data object.
     */
    public DataDTO(Object object) {
        data.add(object);
    }
}
