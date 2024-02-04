package com.spring.todomanagement.todo_mangement.service;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.dto.CommentRequestDto;
import com.spring.todomanagement.todo_mangement.dto.CommentResponseDto;

public interface CommentService {

    CommentResponseDto saveComment(Long todoId, UserDto userDto, CommentRequestDto requestDto);

    CommentResponseDto updateComment(Long todoId, Long commentId, UserDto userDto, CommentRequestDto requestDto);

    Long deleteComment(Long todoId, Long commentId, UserDto userDto);
}
