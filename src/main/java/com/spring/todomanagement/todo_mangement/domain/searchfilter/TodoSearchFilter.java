package com.spring.todomanagement.todo_mangement.domain.searchfilter;

import com.spring.todomanagement.todo_mangement.domain.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TodoSearchFilter {

    private String title;
    private Long userId;
    @Builder.Default
    private TodoStatus todoStatus = TodoStatus.NOT_DONE;
}
