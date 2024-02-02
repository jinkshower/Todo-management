package com.spring.todomanagement.web;

import com.spring.todomanagement.domain.user.Login;
import com.spring.todomanagement.web.dto.TodoPostRequestDto;
import com.spring.todomanagement.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TodoController {

    @PostMapping("/todos")
    public ResponseEntity<Void> postTodo(@Login UserDto userDto, @RequestBody TodoPostRequestDto requestDto) {
        System.out.println("userDto.getName() = " + userDto.getName());
        System.out.println("requestDto = " + requestDto.getTitle());
        return ResponseEntity.ok().build();
    }
}
