package com.example.employeeservice.controller;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.dto.EmployeeResponseDTO;
import com.example.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeResponseDTO> getAll() {
        logger.info("Fetching all employees");
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public EmployeeResponseDTO create(@Valid @RequestBody EmployeeRequestDTO requestDTO) {
        logger.info("Received request to create employee: {}", requestDTO);
        return employeeService.saveEmployee(requestDTO);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO get(@PathVariable Long id) {
        logger.info("Fetching employee with id: {}", id);
        return employeeService.getByIdOrThrow(id);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting employee with id: {}", id);
        employeeService.deleteById(id);
    }
}

