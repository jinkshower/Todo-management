package com.spring.todomanagement.todo_mangement.controller;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.auth.support.Login;
import com.spring.todomanagement.common.CommonResponse;
import com.spring.todomanagement.todo_mangement.dto.FollowResponseDto;
import com.spring.todomanagement.todo_mangement.service.FollowService;
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
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follows/{toUserId}")
    public ResponseEntity<CommonResponse<FollowResponseDto>> createFollow(
        @Login UserDto userDto,
        @PathVariable Long toUserId) {
        FollowResponseDto responseDto = followService.createFollow(userDto, toUserId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<FollowResponseDto>builder()
                .message("팔로우가 성공하였습니다.")
                .data(responseDto)
                .build());
    }
}
