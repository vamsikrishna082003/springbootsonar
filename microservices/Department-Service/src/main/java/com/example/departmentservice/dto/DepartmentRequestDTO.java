package com.example.departmentservice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String code;
}
