package com.prac.repository.author;

import com.prac.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {

}
