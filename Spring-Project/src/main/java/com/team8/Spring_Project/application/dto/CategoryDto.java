package com.team8.Spring_Project.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    Long id;
    String name;
    String icon;

}