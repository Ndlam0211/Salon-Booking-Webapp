package com.lamnd.service;

import com.lamnd.dto.response.BookingResponse;

import java.util.List;
import java.util.Map;

public interface BookingChartService {

        // generate daily earnings chart data
        List<Map<String, Object>> generateDailyEarningsChartData(List<BookingResponse> bookings);

        // generate daily bookings chart data
        List<Map<String, Object>> generateDailyBookingsChartData(List<BookingResponse> bookings);
}
