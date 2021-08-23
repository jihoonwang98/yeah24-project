package com.prac.api;

import com.prac.dto.AuthorDTO;
import com.prac.dto.AuthorDTO.AuthorDetailDTO;
import com.prac.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/authors")
public class AuthorApi {

    private final AuthorService authorService;


    @GetMapping("/{id}")
    public ResponseEntity<AuthorDetailDTO> findAuthorDetail(@PathVariable("id") Long id) {
        AuthorDetailDTO dto = authorService.findAuthorDetailById(id);
        return ResponseEntity.ok(dto);
    }
}
