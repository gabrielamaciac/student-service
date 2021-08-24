package com.learning.student.studentservice.persistance.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Address {
    private String street;
    private String number;
    private String city;
    private String country;
}
