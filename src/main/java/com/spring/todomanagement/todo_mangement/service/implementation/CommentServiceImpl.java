package com.spring.todomanagement.todo_mangement.service.implementation;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.domain.Comment;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.dto.CommentRequestDto;
import com.spring.todomanagement.todo_mangement.dto.CommentResponseDto;
import com.spring.todomanagement.todo_mangement.exception.InvalidCommentException;
import com.spring.todomanagement.todo_mangement.exception.InvalidTodoException;
import com.spring.todomanagement.todo_mangement.exception.InvalidUserException;
import com.spring.todomanagement.todo_mangement.repository.CommentRepository;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.todo_mangement.service.CommentService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CommentResponseDto saveComment(Long todoId, UserDto userDto,
        CommentRequestDto requestDto) {
        Todo todo = findTodo(todoId);
        User user = findUser(userDto.getUserId());

        Comment entity = requestDto.toEntity(user, todo);
        commentRepository.save(entity);

        return new CommentResponseDto(entity);
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(Long todoId,
        Long commentId,
        UserDto userDto,
        CommentRequestDto requestDto) {
        User user = findUser(userDto.getUserId());
        Todo todo = findTodo(todoId);
        Comment comment = findComment(commentId);

        comment.update(user, todo, requestDto);
        return new CommentResponseDto(comment);
    }

    @Transactional
    @Override
    public Long deleteComment(Long todoId, Long commentId, UserDto userDto) {
        User user = findUser(userDto.getUserId());
        findTodo(todoId);
        Comment comment = findComment(commentId);

        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            String errorMessage = "댓글 삭제 실패 - 작성자 id 불일치. 댓글 작성자 ID: " + comment.getUser().getId()
                + ", 요청 사용자 ID: " + user.getId();
            log.error(errorMessage);
            throw new InvalidUserException(errorMessage);
        }
        commentRepository.delete(comment);
        return comment.getId();
    }

    private Todo findTodo(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
            () -> {
                String errorMessage = "해당하는 할 일이 없습니다. 요청 ID: " + todoId;
                log.error(errorMessage);
                return new InvalidTodoException(errorMessage);
            }
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> {
                String errorMessage = "해당하는 댓글이 없습니다. 요청 ID: " + commentId;
                log.error(errorMessage);
                return new InvalidCommentException(errorMessage);
            }
        );
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> {
                String errorMessage = String.format("없는 유저입니다. 요청 ID: " + userId);
                log.error(errorMessage);
                return new InvalidUserException(errorMessage);
            }
        );
    }
}
