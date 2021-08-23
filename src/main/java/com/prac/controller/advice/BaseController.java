package com.prac.controller.advice;

import com.prac.dto.CategoryDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackages = "com.prac.controller")
@RequiredArgsConstructor
@Slf4j
public class BaseController {

    private final CategoryService categoryService;

    @ModelAttribute("parents")
    public Map<String, List<CategoryBriefDTO>> parentCategories() {
        Map<String, List<CategoryBriefDTO>> map = new HashMap<>();
        map.put("categories", categoryService.findParentCategories());
        return map;
    }

}
