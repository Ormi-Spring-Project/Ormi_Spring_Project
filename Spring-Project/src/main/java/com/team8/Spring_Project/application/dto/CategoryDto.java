package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Category;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    Long id;

    String name;

    String icon;

    public static CategoryDto fromEntity(Category category) {

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .build();

    }


}