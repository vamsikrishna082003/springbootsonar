package com.example.departmentservice.controller;

import com.example.departmentservice.dto.DepartmentRequestDTO;
import com.example.departmentservice.dto.DepartmentResponseDTO;
import com.example.departmentservice.exception.GlobalExceptionHandler;
import com.example.departmentservice.exception.ResourceNotFoundException;
import com.example.departmentservice.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DepartmentController.class)
@Import({GlobalExceptionHandler.class, DepartmentControllerTest.TestConfig.class})
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentService departmentService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public DepartmentService departmentService() {
            return Mockito.mock(DepartmentService.class);
        }
    }

    @Test
    void testGetAllDepartments() throws Exception {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(1L);
        dto.setName("Finance");
        dto.setCode("FIN-01");

        when(departmentService.getAllDepartments()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Finance"));
    }

    @Test
    void testCreateDepartment() throws Exception {
        DepartmentRequestDTO request = new DepartmentRequestDTO();
        request.setName("IT");
        request.setCode("IT-01");

        DepartmentResponseDTO response = new DepartmentResponseDTO();
        response.setId(2L);
        response.setName("IT");
        response.setCode("IT-01");

        when(departmentService.saveDepartment(any(DepartmentRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IT"))
                .andExpect(jsonPath("$.code").value("IT-01"));
    }

    @Test
    void testGetDepartmentById_Found() throws Exception {
        DepartmentResponseDTO response = new DepartmentResponseDTO();
        response.setId(1L);
        response.setName("Admin");
        response.setCode("ADM-01");

        when(departmentService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin"))
                .andExpect(jsonPath("$.code").value("ADM-01"));
    }

    @Test
    void testGetDepartmentById_NotFound() throws Exception {
        when(departmentService.getById(99L))
                .thenThrow(new ResourceNotFoundException("Department not found with ID: 99"));

        mockMvc.perform(get("/api/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Department not found with ID: 99"));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteById(1L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetDepartmentById_InternalError_ShouldReturn500() throws Exception {
        when(departmentService.getById(1L)).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected Error"));
    }
}
