package de.htw.ai.wad.advi.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
@Builder
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    
    @Column(name = "userid")
    @Length(min = 5, message = "*Your userid must have at least 5 characters")
    @NotEmpty(message = "*Please provide a userid")
    private String userId;
    
    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    
    @Column(name = "firstname")
    @NotEmpty(message = "*Please provide your first name")
    private String firstName;
    
    @Column(name = "lastname")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;
    
    @Column(name = "isAdmin")
    private Boolean isAdmin;
}