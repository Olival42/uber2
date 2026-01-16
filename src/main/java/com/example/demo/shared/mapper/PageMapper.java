package com.example.demo.shared.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.demo.shared.PageResponse;

@Component
public final class PageMapper {

    public static <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

}
