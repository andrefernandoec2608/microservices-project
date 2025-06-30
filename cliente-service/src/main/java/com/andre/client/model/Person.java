package com.andre.client.model;

import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {

    private String name;
    private String gender;
    private Integer age;
    private String identification;
    private String address;
    private String phone;
}
