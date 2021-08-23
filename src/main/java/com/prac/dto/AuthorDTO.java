package com.prac.dto;

import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.dto.BookDTO.BookAuthorDTO;
import com.prac.dto.BookDTO.BookDetailDTO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorDTO {

    @Data
    public static class AuthorBriefDTO {
        private Long id;
        private String name;
        private String bio;
        private String imgSrc;

        public AuthorBriefDTO(Author author) {
            this.bio = author.getBio();
            this.id = author.getId();
            this.name = author.getName();
            this.imgSrc = author.getImgSrc();
        }
    }


    @Data
    public static class AuthorDetailDTO {
        private Long id;
        private String name;
        private String bio;
        private String imgSrc;

        private List<BookAuthorDTO> books;

        public AuthorDetailDTO(Author author, List<Book> books) {
            this.id = author.getId();
            this.name = author.getName();
            this.bio = author.getBio();
            this.imgSrc = author.getImgSrc();

            this.books = books.stream().map(b -> new BookAuthorDTO(b)).collect(Collectors.toList());
        }
    }
}
