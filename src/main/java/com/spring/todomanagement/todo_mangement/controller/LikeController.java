package com.spring.todomanagement.todo_mangement.controller;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.auth.support.Login;
import com.spring.todomanagement.common.CommonResponse;
import com.spring.todomanagement.todo_mangement.dto.LikeResponseDto;
import com.spring.todomanagement.todo_mangement.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes/{postId}")
    public ResponseEntity<CommonResponse<LikeResponseDto>> createFollow(
        @Login UserDto userDto,
        @PathVariable Long postId) {
        LikeResponseDto responseDto = likeService.createLike(userDto, postId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<LikeResponseDto>builder()
                .message("팔로우가 성공하였습니다.")
                .data(responseDto)
                .build());
    }
}
