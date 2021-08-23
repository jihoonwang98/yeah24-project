package com.prac.error.exception.notFound;

import com.prac.error.ErrorCode;

public class AuthorNotFoundException extends EntityNotFoundException {

    public AuthorNotFoundException() {
        super(ErrorCode.AUTHOR_NOT_FOUND);
    }
}
