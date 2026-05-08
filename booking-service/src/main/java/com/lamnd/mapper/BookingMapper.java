package com.lamnd.mapper;

import com.lamnd.common.BaseMapper;
import com.lamnd.dto.request.BookingCreateRequest;
import com.lamnd.dto.request.BookingUpdateRequest;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<Booking, BookingResponse, BookingCreateRequest, BookingUpdateRequest> {
}
