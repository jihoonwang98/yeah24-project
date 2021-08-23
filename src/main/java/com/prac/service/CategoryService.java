package com.prac.service;

import com.prac.domain.Category;
import com.prac.dto.CategoryDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.prac.dto.CategoryDTO.CategoryDetailDTO;
import com.prac.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<CategoryBriefDTOWithChildren> findParentCategoriesWithChildren() {
        return categoryRepository.findParentCategoriesWithChildren();
    }

    public CategoryDetailDTO findCategoryDetailById(Long id) {
        Category category = findCategoryById(id);
        return new CategoryDetailDTO(category);
    }

    private Category findCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return category;
    }

    public List<CategoryBriefDTO> findParentCategories() {
        return categoryRepository.findParentCategories();
    }


    public CategoryBriefDTOWithChildren findCategoryWithChildrenById(Long id) {
        return categoryRepository.findCategoryWithChildren(id);
    }
}
