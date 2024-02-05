package com.spring.todomanagement.todo_mangement.controller;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.auth.support.Login;
import com.spring.todomanagement.common.CommonResponse;
import com.spring.todomanagement.todo_mangement.dto.CommentRequestDto;
import com.spring.todomanagement.todo_mangement.dto.CommentResponseDto;
import com.spring.todomanagement.todo_mangement.service.CommentService;
import com.spring.todomanagement.todo_mangement.service.implementation.CommentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/todos/{todoId}/comments")
    public ResponseEntity<CommonResponse<CommentResponseDto>> saveComment(
            @PathVariable Long todoId,
            @Login UserDto userDto,
            @RequestBody @Valid CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.saveComment(todoId, userDto, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(CommonResponse.<CommentResponseDto>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("댓글이 생성되었습니다.")
                        .data(responseDto).build());
    }

    @PatchMapping("/todos/{todoId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(
            @PathVariable Long todoId,
            @PathVariable Long commentId,
            @Login UserDto userDto,
            @RequestBody @Valid CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.updateComment(todoId, commentId, userDto, requestDto);
        return ResponseEntity.ok().body(CommonResponse.<CommentResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글이 수정되었습니다")
                        .data(responseDto).build());
    }

    @DeleteMapping("/todos/{todoId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<Long>> deleteComment(
            @PathVariable Long todoId,
            @PathVariable Long commentId,
            @Login UserDto userDto) {
        Long deletedCommentId = commentService.deleteComment(todoId, commentId, userDto);
        return ResponseEntity.ok().body(CommonResponse.<Long>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글이 삭제되었습니다.")
                        .data(deletedCommentId).build());
    }
}
