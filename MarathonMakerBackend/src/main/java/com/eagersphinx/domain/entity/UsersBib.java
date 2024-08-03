package com.eagersphinx.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "user_bib")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsersBib {

    @Id
    @Column(name = "bib")
    private int bib;
    @Column(name = "user_id")
    private int userId;

    @Column(name = "event_id")
    private int eventId;

}
