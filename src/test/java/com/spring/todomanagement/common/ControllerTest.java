package com.spring.todomanagement.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.todomanagement.auth.support.JwtUtil;
import com.spring.todomanagement.todo_mangement.controller.TodoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
@Import(ExternalConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtUtil jwtUtil;
}
