package com.prac.repository.user;

import com.prac.domain.QUser;
import com.prac.domain.User;
import com.prac.dto.UserDTO;
import com.prac.dto.UserDTO.UserDetailDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.prac.domain.QUser.user;

@Transactional
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO findUserDetailDTO(Long id) {
        return queryFactory
                .select(Projections.constructor(UserDetailDTO.class, user))
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

}
