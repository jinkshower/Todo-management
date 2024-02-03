package com.spring.todomanagement.web;

import com.spring.todomanagement.domain.user.Login;
import com.spring.todomanagement.web.dto.CommentResponseDto;
import com.spring.todomanagement.web.dto.CommentSaveRequestDto;
import com.spring.todomanagement.service.comment.CommentService;
import com.spring.todomanagement.web.dto.UserDto;
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
            @RequestBody CommentSaveRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.saveComment(todoId, userDto, requestDto);
        return ResponseEntity.ok().body(CommonResponse.<CommentResponseDto>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("댓글이 생성되었습니다.")
                        .data(responseDto).build());
    }

}
