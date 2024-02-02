package com.spring.todomanagement.web;

import com.spring.todomanagement.domain.user.Login;
import com.spring.todomanagement.service.todo.TodoService;
import com.spring.todomanagement.web.dto.TodoSaveRequestDto;
import com.spring.todomanagement.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<Void> postTodo(@Login UserDto userDto, @RequestBody TodoSaveRequestDto requestDto) {
        log.info(String.valueOf(userDto.getUser().getId()));
        log.info(userDto.getUser().getName());
        log.info(userDto.getUser().getPassword());
        return ResponseEntity.ok().build();
    }
}
