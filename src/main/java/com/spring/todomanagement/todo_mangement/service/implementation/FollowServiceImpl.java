package com.spring.todomanagement.todo_mangement.service.implementation;

import com.spring.todomanagement.auth.dto.UserDto;
import com.spring.todomanagement.todo_mangement.domain.Follow;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.dto.FollowResponseDto;
import com.spring.todomanagement.todo_mangement.exception.InvalidUserException;
import com.spring.todomanagement.todo_mangement.repository.FollowRepository;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.todo_mangement.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    public FollowResponseDto createFollow(UserDto userDto, Long toUserId) {
        User follower = findUser(userDto.getUserId());
        User following = findUser(toUserId);
        Follow follow = Follow.builder()
            .follower(follower)
            .following(following)
            .build();
        followRepository.save(follow);
        return new FollowResponseDto(follow);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new InvalidUserException("없는 유저입니다.")
        );
    }
}
