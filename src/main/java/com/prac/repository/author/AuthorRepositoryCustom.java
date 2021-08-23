package com.prac.repository.author;

import com.prac.domain.Author;
import com.prac.dto.AuthorDTO;
import com.prac.dto.AuthorDTO.AuthorDetailDTO;

import java.util.List;
import java.util.Set;

public interface AuthorRepositoryCustom {

    AuthorDetailDTO findAuthorDetailById(Long id);
}
