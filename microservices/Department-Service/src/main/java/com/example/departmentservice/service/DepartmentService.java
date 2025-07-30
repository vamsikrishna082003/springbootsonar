package com.example.departmentservice.service;

import com.example.departmentservice.dto.DepartmentRequestDTO;
import com.example.departmentservice.dto.DepartmentResponseDTO;
import com.example.departmentservice.model.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(dept -> modelMapper.map(dept, DepartmentResponseDTO.class))
                .collect(Collectors.toList());
    }

    public DepartmentResponseDTO saveDepartment(DepartmentRequestDTO requestDTO) {
        Department department = modelMapper.map(requestDTO, Department.class);
        Department saved = departmentRepository.save(department);
        return modelMapper.map(saved, DepartmentResponseDTO.class);
    }

    public Optional<DepartmentResponseDTO> getById(Long id) {
        return departmentRepository.findById(id)
                .map(dept -> modelMapper.map(dept, DepartmentResponseDTO.class));
    }

    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
