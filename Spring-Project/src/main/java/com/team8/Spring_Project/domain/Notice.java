package com.team8.Spring_Project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "Notice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = true, length = 255 기본값.
    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(String title,
                       String content,
                       Timestamp updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }

}
