package com.spring.todomanagement.todo_mangement.domain;

import com.spring.todomanagement.todo_mangement.domain.searchfilter.AuthorSearchFilter;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.SearchFilter;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.StatusSearchFilter;
import com.spring.todomanagement.todo_mangement.domain.searchfilter.TitleSearchFilter;
import com.spring.todomanagement.todo_mangement.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Todos {

    private final List<Todo> todos;
    private final List<SearchFilter> filters = new ArrayList<>();

    public Todos(List<Todo> todos) {
        this.todos = new ArrayList<>(todos);
        initializeFilters();
    }

    private void initializeFilters() {
        filters.add(new AuthorSearchFilter());
        filters.add(new TitleSearchFilter());
        filters.add(new StatusSearchFilter());
    }

    public List<Todo> filter(Object ...parameters) {
        List<Todo> filtered = new ArrayList<>(todos);

        for (Object object: parameters) {
            if (object == null) {
                continue;
            }
            SearchFilter searchFilter = findFilter(object);
            filtered = searchFilter.apply(filtered, object);
        }

        return filtered.stream()
                .sorted(Comparator.comparing(Timestamped::getCreatedAt).reversed())
                .toList();
    }

    private SearchFilter findFilter(Object object) {
        return filters.stream()
                .filter(filter -> filter.supports(object))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("유효한 입력이 아닙니다."));
    }
}
