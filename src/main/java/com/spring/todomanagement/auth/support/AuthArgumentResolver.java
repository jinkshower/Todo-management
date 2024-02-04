package com.spring.todomanagement.auth.support;

import com.spring.todomanagement.auth.exception.AuthenticationException;
import com.spring.todomanagement.auth.exception.InvalidTokenException;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import com.spring.todomanagement.auth.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = jwtUtil.getJwtFromHeader(request);
        if (!jwtUtil.validateToken(token)) {
            log.error("토큰 검증에 실패했습니다.");
            throw new InvalidTokenException();
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        User found = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("id로 유저를 찾을 수 없습니다.");
                    return new AuthenticationException("없는 유저입니다.");
                }
        );
        log.debug("검증 통과!");

        return new UserDto(found);
    }
}
