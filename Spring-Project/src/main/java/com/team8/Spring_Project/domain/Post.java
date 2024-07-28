package com.team8.Spring_Project.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "Post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = true, length = 255 기본값.
    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String application;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true) // 이거 false로 교체 해야함.
    private Category category;

    public void update(String title,
                       String content,
                       String application,
                       Timestamp createdAt,
                       Timestamp updatedAt) {

        this.title = title;
        this.content = content;
        this.application = application;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

}
