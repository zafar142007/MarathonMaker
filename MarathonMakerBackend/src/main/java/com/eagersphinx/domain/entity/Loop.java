package com.eagersphinx.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity(name = "loop")
@Table(name = "loop", uniqueConstraints = @UniqueConstraint(columnNames = {"bib", "loop"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Loop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "checkpoint")
    private String checkpoint;

    @Column(name = "bib")
    private Integer bib;

    @Column(name = "loop")
    private Integer loop;

    @Column(name = "lat")
    private Double lat;
    @Column(name = "long")
    private Double longitude;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;


}
