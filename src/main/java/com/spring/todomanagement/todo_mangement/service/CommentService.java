package com.spring.todomanagement.todo_mangement.service;

import com.spring.todomanagement.todo_mangement.domain.Comment;
import com.spring.todomanagement.todo_mangement.repository.CommentRepository;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.todo_mangement.dto.CommentResponseDto;
import com.spring.todomanagement.todo_mangement.dto.CommentRequestDto;
import com.spring.todomanagement.auth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto saveComment(Long todoId, UserDto userDto, CommentRequestDto requestDto) {
        Todo todo = findTodo(todoId);

        Comment entity = requestDto.toEntity(userDto.getUser(), todo);
        commentRepository.save(entity);

        return new CommentResponseDto(entity);
    }

    @Transactional
    public CommentResponseDto updateComment(Long todoId,
                                            Long commentId,
                                            UserDto userDto,
                                            CommentRequestDto requestDto) {
        User user = userDto.getUser();
        Todo todo = findTodo(todoId);
        Comment comment = findComment(commentId);

        comment.update(user, todo, requestDto);
        return new CommentResponseDto(comment);
    }

    public Long deleteComment(Long todoId, Long commentId, UserDto userDto) {
        User user = userDto.getUser();
        Todo todo = findTodo(todoId);
        Comment comment = findComment(commentId);

        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("작성자만 댓글을 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
        return comment.getId();
    }

    private Todo findTodo(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 할 일이 없습니다.")
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 댓글이 없습니다.")
        );
    }
}
