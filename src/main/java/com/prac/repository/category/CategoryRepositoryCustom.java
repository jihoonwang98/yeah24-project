package com.prac.repository.category;

import com.prac.dto.CategoryDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<CategoryBriefDTOWithChildren> findParentCategoriesWithChildren();

    List<CategoryBriefDTO> findParentCategories();

    CategoryBriefDTOWithChildren findCategoryWithChildren(Long id);
}
