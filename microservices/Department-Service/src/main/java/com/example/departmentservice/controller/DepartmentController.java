package com.example.departmentservice.controller;

import com.example.departmentservice.dto.DepartmentRequestDTO;
import com.example.departmentservice.dto.DepartmentResponseDTO;
import com.example.departmentservice.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<DepartmentResponseDTO> getAll() {
        return departmentService.getAllDepartments();
    }

    @PostMapping
    public DepartmentResponseDTO create(@Valid @RequestBody DepartmentRequestDTO requestDTO) {
        return departmentService.saveDepartment(requestDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getById(id));
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departmentService.deleteById(id);
    }
}
