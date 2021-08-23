package com.prac.api;

import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.BookListResponse;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.prac.dto.CategoryDTO.CategoryDetailDTO;
import com.prac.dto.PageRequestDTO;
import com.prac.service.BookService;
import com.prac.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/categories")
public class CategoryApi {

    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping("/parents")
    public ResponseEntity<List<CategoryBriefDTOWithChildren>> findParentCategories() {
        List<CategoryBriefDTOWithChildren> parentCategories = categoryService.findParentCategoriesWithChildren();
        return ResponseEntity.ok(parentCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailDTO> findCategoryDetailById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.findCategoryDetailById(id));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<BookListResponse> findBooksInCategory(@PathVariable("id") Long id, @RequestBody PageRequestDTO pageRequestDTO) {
        BookListResponse bookListResponse = bookService.findBooksByCategoryId(id, pageRequestDTO.of(), pageRequestDTO.getOrderBy());
        return ResponseEntity.ok(bookListResponse);
    }
}
