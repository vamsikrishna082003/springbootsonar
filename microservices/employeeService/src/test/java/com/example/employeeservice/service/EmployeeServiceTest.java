package com.example.employeeservice.service;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.dto.EmployeeResponseDTO;
import com.example.employeeservice.exception.NotFoundException;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeRequestDTO requestDTO;
    private EmployeeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee(1L, "John", "john@example.com", "pass", 101L);
        requestDTO = new EmployeeRequestDTO();
        requestDTO.setName("John");
        requestDTO.setEmail("john@example.com");
        requestDTO.setPassword("pass");
        requestDTO.setDepartmentId(101L);
        responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("John");
        responseDTO.setEmail("john@example.com");
        responseDTO.setDepartmentId(101L);
    }

    // 1. Save employee - success
    @Test
    void testSaveEmployee_Success() {
        when(modelMapper.map(requestDTO, Employee.class)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeResponseDTO.class)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.saveEmployee(requestDTO);

        assertEquals("John", result.getName());
        assertEquals(101L, result.getDepartmentId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // 2. Get by ID - found
    @Test
    void testGetByIdOrThrow_Found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeResponseDTO.class)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.getByIdOrThrow(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(employeeRepository).findById(1L);
    }

    // 3. Get by ID - not found
    @Test
    void testGetByIdOrThrow_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            employeeService.getByIdOrThrow(1L);
        });

        assertEquals("Employee not found with id: 1", exception.getMessage());
    }

    // 4. Get all employees - returns list
    @Test
    void testGetAllEmployees_WithResults() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(modelMapper.map(employee, EmployeeResponseDTO.class)).thenReturn(responseDTO);

        List<EmployeeResponseDTO> list = employeeService.getAllEmployees();

        assertEquals(1, list.size());
        assertEquals("John", list.get(0).getName());
    }

    // 5. Get all employees - empty list
    @Test
    void testGetAllEmployees_Empty() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeResponseDTO> list = employeeService.getAllEmployees();

        assertTrue(list.isEmpty());
    }

    // 6. Delete by ID - success
    @Test
    void testDeleteById_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        assertDoesNotThrow(() -> employeeService.deleteById(1L));
        verify(employeeRepository).deleteById(1L);
    }

    // 7. Delete by ID - not found
    @Test
    void testDeleteById_NotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            employeeService.deleteById(1L);
        });

        assertEquals("Cannot delete. Employee not found with id: 1", exception.getMessage());
    }

    // 8. Save employee - verify mapping is called
    @Test
    void testSaveEmployee_VerifyModelMapperCalled() {
        when(modelMapper.map(requestDTO, Employee.class)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeResponseDTO.class)).thenReturn(responseDTO);

        employeeService.saveEmployee(requestDTO);

        verify(modelMapper).map(requestDTO, Employee.class);
        verify(modelMapper).map(employee, EmployeeResponseDTO.class);
    }

    // 9. Get by ID - verify mapping
    @Test
    void testGetByIdOrThrow_VerifyModelMapperCalled() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeResponseDTO.class)).thenReturn(responseDTO);

        employeeService.getByIdOrThrow(1L);

        verify(modelMapper).map(employee, EmployeeResponseDTO.class);
    }

    // 10. Delete by ID - verify exists check and deletion
    @Test
    void testDeleteById_VerifyRepositoryMethods() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteById(1L);

        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).deleteById(1L);
    }
}
