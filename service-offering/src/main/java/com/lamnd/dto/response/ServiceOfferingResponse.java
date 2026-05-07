package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ServiceOfferingResponse(
    Long id,
    String name,
    String description,
    Double price,
    int duration,
    String image,
    Long salonId,
    Long categoryId
) {
}
