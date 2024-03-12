package com.spring.todomanagement.todo_mangement.service.implementation;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.domain.Like;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.dto.LikeResponseDto;
import com.spring.todomanagement.todo_mangement.exception.InvalidTodoException;
import com.spring.todomanagement.todo_mangement.repository.LikeRepository;
import com.spring.todomanagement.todo_mangement.repository.TodoRepository;
import com.spring.todomanagement.todo_mangement.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

    private final TodoRepository todoRepository;
    private final LikeRepository likeRepository;

    @Override
    public LikeResponseDto createLike(UserDto userDto, Long todoId) {
        Todo todo = findTodo(todoId);
        Like like = Like.builder()
            .userId(userDto.getUserId())
            .todo(todo)
            .build();
        likeRepository.save(like);
        return new LikeResponseDto(like);
    }

    private Todo findTodo(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
            () -> new InvalidTodoException("없는 할일입니다.")
        );
    }
}
