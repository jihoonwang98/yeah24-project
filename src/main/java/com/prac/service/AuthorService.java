package com.prac.service;

import com.prac.domain.Author;
import com.prac.dto.AuthorDTO;
import com.prac.dto.AuthorDTO.AuthorDetailDTO;
import com.prac.repository.author.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;


    public AuthorDetailDTO findAuthorDetailById(Long id) {
        return authorRepository.findAuthorDetailById(id);
    }
}
