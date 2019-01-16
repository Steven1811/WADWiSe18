package de.htw.ai.wad.advi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    
    @Column(name = "fullname")
    @NotEmpty(message = "*Please provide a full name")
    private String fullname;
    
    @Column(name = "address")
    @NotEmpty(message = "*Please provide an address")
    private String address;
    
    @Column(name = "postcode")
    @NotEmpty(message = "*Please provide a postcode")
    private String postcode;
    
    @Column(name = "city")
    @NotEmpty(message = "*Please provide a city")
    private String city;
    
    @Column(name = "country")
    @NotEmpty(message = "*Please provide a city")
    private String country;
    
    @Column(name = "isPrivate")
    private Boolean isPrivate;
}