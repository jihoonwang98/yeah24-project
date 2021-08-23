package com.prac.dto;

import com.prac.domain.Category;
import com.prac.dto.CategoryDTO.CategoryDetailDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDTOTest {

    Category parent = new Category("parent", null);
    Category child1 = new Category("child1", parent);
    Category child2 = new Category("child2", parent);
    Category grandChild1 = new Category("grandchild1", child1);
    Category grandChild2 = new Category("grandchild2", child2);

    @Test
    void test1() {
        CategoryDetailDTO dto = new CategoryDetailDTO(parent);
        System.out.println(dto);
    }
}