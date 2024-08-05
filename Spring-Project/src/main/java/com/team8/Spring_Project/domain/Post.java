package com.team8.Spring_Project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Post")
@Getter
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "average_rating")
    private Double averageRating;

    public void update(String title,
                       byte[] picture,
                       String content,
                       String application,
                       Timestamp updatedAt,
                       Category category,
                       double averageRating) {


        this.title = title;
        this.picture = picture;
        this.content = content;
        this.application = application;
        this.updatedAt = updatedAt;
        this.category = category;
        this.averageRating = averageRating;

    }

    public void setAverageRating(double rating) {
        this.averageRating = rating;
    }
}
