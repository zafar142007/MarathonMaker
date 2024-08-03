package com.eagersphinx.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "is_awarded")
    private Boolean isAwarded;

}
