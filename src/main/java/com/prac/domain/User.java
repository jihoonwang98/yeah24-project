package com.prac.domain;

import com.prac.domain.etc.Address;
import com.prac.domain.etc.BaseEntity;
import com.prac.domain.etc.Gender;
import com.prac.domain.etc.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseEntity {

    private String username;

    private String password;

    private String nickname;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private Address address;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private String providerId;

    private String profileImg;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "wish_list",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_wishlist_to_user")),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id", foreignKey = @ForeignKey(name = "fk_wishlist_to_book"))
    )
    private Set<Book> wishlist = new HashSet<>();


    @Builder
    public User(String username, String password, String nickname, String name, Gender gender, Address address, String email, Role role, String provider, String providerId, String profileImg) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImg = profileImg;
    }

    public void addToWishlist(Book... books) {
        this.wishlist.addAll(Arrays.asList(books));
    }

    public void addToWishlist(List<Book> books) {
        this.wishlist.addAll(books);
    }

    public void deleteInWishlist(Book book) {
        this.wishlist.remove(book);
    }

    public void modify(String password, String nickname, String profileImg, Address address) {
        this.password = password;
        this.nickname = nickname;

        if (profileImg != null) {
            this.profileImg = profileImg;
        }

        this.address = new Address(address.getPostcode(), address.getRoadAddr(), address.getJibunAddr(), address.getDetailAddr(), address.getExtraAddr());
    }

}
