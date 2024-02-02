package com.spring.todomanagement.auth;

import com.spring.todomanagement.domain.user.Login;
import com.spring.todomanagement.domain.user.User;
import com.spring.todomanagement.domain.user.UserRepository;
import com.spring.todomanagement.web.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

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
        log.info(token);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        String name = jwtUtil.getUserInfoFromToken(token).getSubject();
        User found = userRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("없는 이름입니다.")
        );

        return new UserDto(found);
    }
}
