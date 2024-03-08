package com.spring.todomanagement.todo_mangement.controller;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.auth.support.Login;
import com.spring.todomanagement.common.CommonResponse;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TodoSearchFilter;
import com.spring.todomanagement.todo_mangement.dto.TodoRequestDto;
import com.spring.todomanagement.todo_mangement.dto.TodoResponseDto;
import com.spring.todomanagement.todo_mangement.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<CommonResponse<TodoResponseDto>> saveTodo(@Login UserDto userDto,
        @RequestBody @Valid TodoRequestDto requestDto) {
        TodoResponseDto responseDto = todoService.saveTodo(userDto, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(CommonResponse.<TodoResponseDto>builder()
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

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<CommonResponse<TodoResponseDto>> getTodo(@PathVariable Long todoId) {
        TodoResponseDto todoResponseDto = todoService.getTodo(todoId);
        return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
            .statusCode(HttpStatus.OK.value())
            .message("해당 할 일이 조회되었습니다.")
            .data(todoResponseDto).build());
    }

    @GetMapping("/todos/filter")
    public ResponseEntity<CommonResponse<List<TodoResponseDto>>> searchTodos(
        @ModelAttribute("todoSearchFilter") TodoSearchFilter todoSearchFilter) {
        List<TodoResponseDto> todoResponseDtos = todoService.searchTodos(todoSearchFilter);
        log.info(String.valueOf(todoSearchFilter.getUserId()));
        return ResponseEntity.ok().body(CommonResponse.<List<TodoResponseDto>>builder()
            .statusCode(HttpStatus.OK.value())
            .message("검색 결과가 조회되었습니다.")
            .data(todoResponseDtos).build());
    }

    @PatchMapping("/todos/{todoId}")
    public ResponseEntity<CommonResponse<TodoResponseDto>> updateTodo(
        @PathVariable Long todoId,
        @Login UserDto userDto,
        @RequestBody @Valid TodoRequestDto requestDto) {
        TodoResponseDto todoResponseDto = todoService.updateTodo(todoId, userDto, requestDto);
        return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
            .statusCode(HttpStatus.OK.value())
            .message("해당 할 일이 수정되었습니다.")
            .data(todoResponseDto).build());
    }

    @PatchMapping("/todos/{todoId}/status")
    public ResponseEntity<CommonResponse<Long>> changeTodoStatus(
        @PathVariable Long todoId,
        @Login UserDto userDto) {
        Long changedTodoId = todoService.changeTodoStatus(todoId, userDto);
        return ResponseEntity.ok().body(CommonResponse.<Long>builder()
            .statusCode(HttpStatus.OK.value())
            .message("해당 할일의 상태가 변경되었습니다.")
            .data(changedTodoId).build());
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<CommonResponse<Long>> deleteTodo(
        @PathVariable Long todoId,
        @Login UserDto userDto) {
        Long deletedTodoId = todoService.deleteTodo(todoId, userDto);
        return ResponseEntity.ok().body(CommonResponse.<Long>builder()
            .statusCode(HttpStatus.OK.value())
            .message("해당 할일이 삭제되었습니다.")
            .data(deletedTodoId).build());
    }
}
