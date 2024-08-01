package com.team8.Spring_Project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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
    private String tag;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] picture;

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
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void update(String title,
                       String content,
                       String tag,
                       String application,
                       Timestamp updatedAt,
                       Category category) {


        this.title = title;
        this.content = content;
        this.tag = tag;
        this.application = application;
        this.updatedAt = updatedAt;
        this.category = category;

    }

}
