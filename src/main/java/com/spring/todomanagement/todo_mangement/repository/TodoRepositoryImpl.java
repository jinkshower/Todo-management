package com.spring.todomanagement.todo_mangement.repository;

import static com.spring.todomanagement.todo_mangement.domain.QComment.comment;
import static com.spring.todomanagement.todo_mangement.domain.QTodo.todo;
import static com.spring.todomanagement.todo_mangement.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.todomanagement.todo_mangement.domain.Todo;
import com.spring.todomanagement.todo_mangement.domain.TodoStatus;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TodoSearchFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.thymeleaf.util.StringUtils;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Todo> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        List<Todo> fetch = queryFactory.select(todo)
            .from(todo)
            .orderBy(todo.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        long totalSize = queryFactory
            .select(todo.count())
            .from(todo)
            .fetchFirst();
        return PageableExecutionUtils.getPage(fetch, pageable, () -> totalSize);
    }

    @Override
    public List<Todo> searchByFilter(TodoSearchFilter todoSearchFilter) {
        return queryFactory
            .select(todo)
            .from(todo)
            .join(todo.user, user).fetchJoin()
            .leftJoin(todo.comments, comment).fetchJoin()
            .where(
                eqAuthor(todoSearchFilter.getUserId()),
                eqTitle(todoSearchFilter.getTitle()),
                eqStatus(todoSearchFilter.getTodoStatus())
            )
            .fetch();
    }

    public Page<Todo> findAll(Pageable pageable) {
        // 페이징 정보를 적용하여 쿼리 실행
        List<Todo> todos = queryFactory
            .select(todo)
            .from(todo)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 전체 개수를 조회
        long totalSize = queryFactory
            .select(todo.count())
            .from(todo)
            .fetchFirst();

        // 페이징 처리된 결과를 Page 객체로 변환하여 반환
//        return new PageImpl<>(todos, pageable, totalSize);
        return PageableExecutionUtils.getPage(todos, pageable, () -> totalSize);
    }

    private BooleanExpression eqTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            return null;
        }
        return todo.title.eq(title);
    }

    private BooleanExpression eqAuthor(Long userId) {
        if (userId == null) {
            return null;
        }
        return todo.id.eq(userId);
    }

    private BooleanExpression eqStatus(TodoStatus status) {
        return todo.todoStatus.eq(status);
    }
}
