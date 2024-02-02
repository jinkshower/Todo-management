package com.spring.todomanagement.web;

import com.spring.todomanagement.domain.user.Login;
import com.spring.todomanagement.service.todo.TodoService;
import com.spring.todomanagement.web.dto.TodoResponseDto;
import com.spring.todomanagement.web.dto.TodoSaveRequestDto;
import com.spring.todomanagement.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<CommonResponse<TodoResponseDto>> saveTodo(@Login UserDto userDto, @RequestBody TodoSaveRequestDto requestDto) {
        log.info(String.valueOf(userDto.getUser().getId()));
        log.info(userDto.getUser().getName());
        log.info(userDto.getUser().getPassword());

        TodoResponseDto responseDto = todoService.saveTodo(userDto.getUser(), requestDto);
        return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("할일이 등록되었습니다.")
                        .data(responseDto).build());
    }

    @GetMapping("/todos")
    public ResponseEntity<CommonResponse<List<TodoResponseDto>>> getAllTodos() {
        List<TodoResponseDto> todoResponseDtos = todoService.getAllTodos();
        return ResponseEntity.ok().body(CommonResponse.<List<TodoResponseDto>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("모든 할일이 조회되었습니다.")
                        .data(todoResponseDtos).build());
    }
}
