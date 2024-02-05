package com.spring.todomanagement.todo_mangement.dto;

import com.spring.todomanagement.todo_mangement.domain.Comment;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @Max(100)
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Builder
    public CommentRequestDto(String content) {
        this.content = content;
    }

    public Comment toEntity(User user, Todo todo) {
        return Comment.builder()
                .content(content)
                .user(user)
                .todo(todo)
                .build();
    }
}
