package com.spring.todomanagement.todo_mangement.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Builder
public class PageDto {

    private int currentPage;
    private int size;
    private String sortBy;

    public Pageable toPageable() {
        if (Objects.isNull(sortBy)) {
            return PageRequest.of(currentPage - 1, size);
        } else {
            return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
        }
    }

    public Pageable toPageable(String sortBy) {
        return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
    }
}
