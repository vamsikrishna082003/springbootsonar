package com.example.departmentservice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentRequestDTO {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Code must not be blank")
    private String code;
}
