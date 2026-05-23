package com.lamnd.common;

public abstract class BaseController {

    protected <T> ApiResponse<T> createSuccessResponse(T data) {
        return ApiResponse.success(data);
    }
}
