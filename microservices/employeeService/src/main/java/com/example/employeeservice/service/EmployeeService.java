package com.example.employeeservice.service;

import com.example.employeeservice.dto.EmployeeRequestDTO;
import com.example.employeeservice.dto.EmployeeResponseDTO;
import com.example.employeeservice.exception.NotFoundException;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;


    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDTO.class))
                .toList();
    }

    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO requestDTO) {
        Employee employee = modelMapper.map(requestDTO, Employee.class);
        Employee saved = employeeRepository.save(employee);
        return modelMapper.map(saved, EmployeeResponseDTO.class);
    }

    public EmployeeResponseDTO getByIdOrThrow(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeResponseDTO.class);
    }

    public void deleteById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("Cannot delete. Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}

