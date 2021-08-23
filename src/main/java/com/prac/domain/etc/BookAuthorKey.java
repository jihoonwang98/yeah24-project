package com.prac.domain.etc;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class BookAuthorKey implements Serializable {

    private Long bookId;

    private Long authorId;
}
