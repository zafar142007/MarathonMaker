package com.eagersphinx.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "marathon_id")
    private int marathonId;

}
