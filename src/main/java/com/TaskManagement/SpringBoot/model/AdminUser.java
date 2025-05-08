package com.TaskManagement.SpringBoot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "admin_users")
public class AdminUser extends User {

}
