package com.works.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class User extends Base {

    private String name;
    @Column(unique = true)
    private String email;
    private String password;


}
