package com.spring.todomanagement.service.comment;

import com.spring.todomanagement.domain.comment.Comment;
import com.spring.todomanagement.domain.comment.CommentRepository;
import com.spring.todomanagement.domain.todo.Todo;
import com.spring.todomanagement.domain.todo.TodoRepository;
import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.domain.user.UserRepository;
import com.spring.todomanagement.web.dto.CommentResponseDto;
import com.spring.todomanagement.web.dto.CommentSaveRequestDto;
import com.spring.todomanagement.web.dto.TodoResponseDto;
import com.spring.todomanagement.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto saveComment(Long todoId, UserDto userDto, CommentSaveRequestDto requestDto) {
        User user = userRepository.findById(userDto.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("없는 사용자입니다.")
        );

        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 할 일이 없습니다.")
        );

        Comment entity = requestDto.toEntity(user, todo);
        commentRepository.save(entity);

        return new CommentResponseDto(entity);
    }
}
