package com.learning.student.studentservice.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @NotBlank
    private String street;
    @NotBlank
    private String number;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
}
