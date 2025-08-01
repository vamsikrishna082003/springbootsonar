package com.example.departmentservice.service;

import com.example.departmentservice.dto.DepartmentRequestDTO;
import com.example.departmentservice.dto.DepartmentResponseDTO;
import com.example.departmentservice.model.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.example.departmentservice.exception.ResourceNotFoundException;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentService(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(dept -> modelMapper.map(dept, DepartmentResponseDTO.class))
                .toList();
    }

    public DepartmentResponseDTO saveDepartment(DepartmentRequestDTO requestDTO) {
        Department department = modelMapper.map(requestDTO, Department.class);
        Department saved = departmentRepository.save(department);
        return modelMapper.map(saved, DepartmentResponseDTO.class);
    }

    public DepartmentResponseDTO getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return modelMapper.map(department, DepartmentResponseDTO.class);
    }

    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
