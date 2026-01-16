package com.example.demo.shared.mapper;

import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.ErrorResponse;

public final class ApiMapper {

    public static <T> ApiResponse<T> sucess(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .error(null)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .data(null)
                .build();
    }

}
