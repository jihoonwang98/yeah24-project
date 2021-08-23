package com.prac.repository.author;

import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.joinTable.BookAuthor;
import com.prac.dto.AuthorDTO.AuthorDetailDTO;
import com.prac.error.exception.notFound.AuthorNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.prac.domain.QAuthor.author;
import static com.prac.domain.QBook.book;
import static com.prac.domain.joinTable.QBookAuthor.bookAuthor;

@RequiredArgsConstructor
@Transactional
public class AuthorRepositoryImpl implements AuthorRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public AuthorDetailDTO findAuthorDetailById(Long id) {

        List<BookAuthor> results = queryFactory
                .select(bookAuthor)
                .from(bookAuthor)
                .join(bookAuthor.author, author).fetchJoin()
                .join(bookAuthor.book, book).fetchJoin()
                .where(bookAuthor.id.authorId.eq(id))
                .fetch();

        if(results.size() == 0) {
            throw new AuthorNotFoundException();
        }

        Author author = results.get(0).getAuthor();
        List<Book> books = results.stream().map(ba -> ba.getBook()).collect(Collectors.toList());
        return new AuthorDetailDTO(author, books);
    }
}
