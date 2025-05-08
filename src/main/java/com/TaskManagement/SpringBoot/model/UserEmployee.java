package com.TaskManagement.SpringBoot.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_employees")
@Getter
@Setter
public class UserEmployee extends User {

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String jobTitle;

}
