package com.prac.dto;

import com.prac.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDTO {

    @Data
    public static class CategoryBriefDTO {
        private Long id;
        private String name;
        private int depth;

        public CategoryBriefDTO(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.depth = category.getDepth();
        }
    }

    @Data
    public static class CategoryBriefDTOWithChildren {
        private Long id;
        private String name;
        private int depth;

        private List<CategoryBriefDTO> children;

        public CategoryBriefDTOWithChildren(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.depth = category.getDepth();
            this.children = category.getChildren().stream().map(c -> new CategoryBriefDTO(c)).collect(Collectors.toList());
        }
    }

    @Data
    @AllArgsConstructor
    @ToString
    public static class CategoryDetailDTO {
        private Long id;
        private String name;
        private int depth;

        private List<CategoryDetailDTO> children;

        public CategoryDetailDTO(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.depth = category.getDepth();

            this.children = category.getChildren().stream().map(c -> new CategoryDetailDTO(c)).collect(Collectors.toList());
        }
    }
}
