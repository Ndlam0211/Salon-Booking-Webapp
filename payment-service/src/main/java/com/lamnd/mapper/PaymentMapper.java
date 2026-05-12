package com.lamnd.mapper;

import com.lamnd.dto.response.PaymentResponse;
import com.lamnd.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
