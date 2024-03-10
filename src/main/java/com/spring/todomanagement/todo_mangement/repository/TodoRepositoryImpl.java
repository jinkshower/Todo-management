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
import org.thymeleaf.util.StringUtils;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

    //    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<Todo> findAllByOrderByCreatedAtDesc() {
        return queryFactory
            .select(todo)
            .from(todo)
            .join(todo.user, user).fetchJoin()
            .leftJoin(todo.comments, comment).fetchJoin()
            .orderBy(todo.createdAt.desc())
            .fetch();
    }

    //    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
