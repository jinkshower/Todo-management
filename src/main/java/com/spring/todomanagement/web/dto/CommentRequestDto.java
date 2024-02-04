package com.spring.todomanagement.web.dto;

import com.spring.todomanagement.domain.comment.Comment;
import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @NotNull
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
