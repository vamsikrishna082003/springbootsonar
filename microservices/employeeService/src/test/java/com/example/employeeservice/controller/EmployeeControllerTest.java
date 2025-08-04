package com.example.employeeservice.controller;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.dto.EmployeeResponseDTO;
import com.example.employeeservice.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(1L);
        dto.setName("John");

        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO();
        request.setName("Alice");

        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setId(2L);
        response.setName("Alice");

        when(employeeService.saveEmployee(request)).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setId(1L);
        response.setName("John");

        when(employeeService.getByIdOrThrow(1L)).thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteById(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllEmployeesEmpty() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    /**
     * Test configuration to mock the service bean and inject it
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        public EmployeeService employeeService() {
            return Mockito.mock(EmployeeService.class);
        }
    }
}
