package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Category;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    Long id;
    String name;

    // Entity -> DTO
    public static CategoryDTO fromEntity(Category category) {

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();

    }

    // DTO -> Entity
    public Category toEntity() {

        return Category.builder()
                .name(this.getName())
                .build();

    }


}