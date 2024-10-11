package com.example.restservice;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "USUARIO", schema = "SYS") // Especificar el esquema
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
}
