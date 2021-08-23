package com.prac.error.exception.notFound;

import com.prac.error.ErrorCode;

public class BookNotFoundException extends EntityNotFoundException {

    public BookNotFoundException() {
        super(ErrorCode.BOOK_NOT_FOUND);
    }
}
