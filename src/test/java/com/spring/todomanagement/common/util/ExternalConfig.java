package com.spring.todomanagement.common.util;

import com.spring.todomanagement.auth.config.WebConfig;
import com.spring.todomanagement.auth.support.AuthArgumentResolver;
import com.spring.todomanagement.auth.support.JwtUtil;
import com.spring.todomanagement.todo_mangement.domain.User;
import com.spring.todomanagement.todo_mangement.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

@TestConfiguration
public class ExternalConfig {

//    @PersistenceContext
//    private EntityManager em;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepository() {
            @Override
            public Optional<User> findByName(String name) {
                return Optional.empty();
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends User> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<User> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public User getOne(Long aLong) {
                return null;
            }

            @Override
            public User getById(Long aLong) {
                return null;
            }

            @Override
            public User getReferenceById(Long aLong) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public <S extends User> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public List<User> findAll() {
                return null;
            }

            @Override
            public List<User> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends User> S save(S entity) {
                return null;
            }

            @Override
            public Optional<User> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(User entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends User> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public List<User> findAll(Sort sort) {
                return null;
            }

            @Override
            public Page<User> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends User> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends User, R> R findBy(Example<S> example,
                Function<FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }
        };
    }

    @Bean
    public WebConfig webConfig() {
        return new WebConfig(new AuthArgumentResolver(jwtUtil(), userRepository()));
    }
//
//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(em);
//    }
//
//    @Bean
//    public TodoQueryRepository todoQueryRepository() {
//        return new TodoQueryRepository(jpaQueryFactory());
//    }
}
