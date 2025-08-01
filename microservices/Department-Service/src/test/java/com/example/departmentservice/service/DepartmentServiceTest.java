package com.example.departmentservice.service;

import com.example.departmentservice.dto.DepartmentRequestDTO;
import com.example.departmentservice.dto.DepartmentResponseDTO;
import com.example.departmentservice.exception.ResourceNotFoundException;
import com.example.departmentservice.model.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private DepartmentRequestDTO requestDTO;
    private DepartmentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department(1L, "Admin", "ADM-01");
        requestDTO = new DepartmentRequestDTO();
        requestDTO.setName("Admin");
        requestDTO.setCode("ADM-01");

        responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Admin");
        responseDTO.setCode("ADM-01");
    }

    @Test
    void testSaveDepartment() {
        when(modelMapper.map(requestDTO, Department.class)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(modelMapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO saved = departmentService.saveDepartment(requestDTO);
        assertEquals("Admin", saved.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testGetDepartmentById() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(modelMapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO found = departmentService.getById(1L);
        assertNotNull(found);
        assertEquals("Admin", found.getName());
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(100L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            departmentService.getById(100L);
        });

        assertEquals("Department not found with ID: 100", exception.getMessage());
    }

    @Test
    void testGetAllDepartments() {
        Department department2 = new Department(2L, "HR", "HR-02");
        DepartmentResponseDTO response2 = new DepartmentResponseDTO();
        response2.setId(2L);
        response2.setName("HR");
        response2.setCode("HR-02");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department, department2));
        when(modelMapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);
        when(modelMapper.map(department2, DepartmentResponseDTO.class)).thenReturn(response2);

        List<DepartmentResponseDTO> list = departmentService.getAllDepartments();
        assertEquals(2, list.size());
    }

    @Test
    void testGetAllDepartments_Empty() {
        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<DepartmentResponseDTO> list = departmentService.getAllDepartments();
        assertTrue(list.isEmpty());
    }

    @Test
    void testDeleteDepartmentById() {
        doNothing().when(departmentRepository).deleteById(1L);
        departmentService.deleteById(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testModelMapperMappingRequestToEntity() {
        when(modelMapper.map(requestDTO, Department.class)).thenReturn(department);
        Department mapped = modelMapper.map(requestDTO, Department.class);
        assertNotNull(mapped);
    }

    @Test
    void testModelMapperMappingEntityToResponse() {
        when(modelMapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);
        DepartmentResponseDTO mapped = modelMapper.map(department, DepartmentResponseDTO.class);
        assertNotNull(mapped);
    }

}
