package com.lamnd.service.impl;

import com.lamnd.entity.Booking;
import com.lamnd.enums.BookingStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingChartServiceImpl {

    // generate daily earnings chart data
    public List<Map<String, Object>> generateDailyEarningsChartData(List<Booking> bookings) {
        // group bookings by date and calculate total earnings for each date
        Map<String, Double> earningsByDay = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getCreatedAt().toLocalDate().toString(),
                        Collectors.summingDouble(Booking::getTotalPrice)
                ));

        // convert the earningsByDay map to a list of maps suitable for charting
        return convertToChartData(earningsByDay, "daily", "earnings");
    }

    // generate daily bookings chart data
    public List<Map<String, Object>> generateDailyBookingsChartData(List<Booking> bookings) {
        // group bookings by date and count the number of bookings for each date
        Map<String, Long> bookingsByDay = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED) // filter out bookings
                .collect(Collectors.groupingBy(
                        booking -> booking.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()
                ));

        return convertToChartData(bookingsByDay, "daily", "count");
    }

    // helper method to convert grouped data to chart data format
    private <T> List<Map<String, Object>> convertToChartData(Map<String, T> groupedData, String periodKey, String dataKey) {
       List<Map<String, Object>> chartData = new ArrayList<>();

       // convert the grouped data to a list of maps with "period" and "data" keys
        groupedData.forEach((period, data) -> {
            Map<String, Object> dataPoint = Map.of(
                    periodKey, period,
                    dataKey, data
            );
            chartData.add(dataPoint);
        });

        // sort the chart data by period (date)
        chartData.sort(Comparator.comparing(dp -> dp.get(periodKey).toString()));
        return chartData;
    }
}
