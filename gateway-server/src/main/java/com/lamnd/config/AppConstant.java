package com.lamnd.config;

public class AppConstant {

    public static final String AUTH_API = "/api/v1/auth/**";
    public static final String NOTIFICATION_WS_API = "/api/v1/notifications/ws/**";
    public static final String[] PUBLIC_APIs = {
            "/api/v1/salons/**",
            "/api/v1/categories/**",
            "/api/v1/notifications/**",
            "/api/v1/bookings/**",
            "/api/v1/payments/**",
            "/api/v1/service-offerings/**",
            "/api/v1/users/**",
            "/api/v1/reviews/**"
    };

    public static final String[] SALON_OWNER_APIs = {
            "/api/v1/notifications/salon-owner/**",
            "/api/v1/categories/salon-owner/**",
            "/api/v1/service-offerings/salon-owner/**"
    };

    public static final String[] ROLEs = {
            "CUSTOMER",
            "SALON_OWNER",
            "ADMIN"
    };
}
