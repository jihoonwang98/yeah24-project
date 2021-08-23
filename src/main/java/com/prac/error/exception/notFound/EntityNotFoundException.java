package com.prac.error.exception.notFound;

import com.prac.error.ErrorCode;
import com.prac.error.exception.BusinessException;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }

    protected EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
