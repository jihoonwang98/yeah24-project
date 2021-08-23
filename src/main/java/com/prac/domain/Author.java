package com.prac.domain;

import com.prac.domain.etc.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@AttributeOverride(name = "id", column = @Column(name = "author_id"))
public class Author extends BaseEntity {

    private String name;

    @Lob
    private String bio;

    private String imgSrc;

    public Author(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    @Builder
    public Author(String name, String bio, String imgSrc) {
        this.name = name;
        this.bio = bio;
        this.imgSrc = imgSrc;
    }
}
