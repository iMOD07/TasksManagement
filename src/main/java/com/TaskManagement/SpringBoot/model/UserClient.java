package com.TaskManagement.SpringBoot.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Table(name = "user_clients")
@Getter
@Setter
@Entity
public class UserClient extends User {

    private String companyName;
    private String address;

}
