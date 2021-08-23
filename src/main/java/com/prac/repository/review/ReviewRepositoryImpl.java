package com.prac.repository.review;

import com.prac.domain.QUser;
import com.prac.domain.User;
import com.prac.domain.joinTable.QReview;
import com.prac.domain.joinTable.Review;
import com.prac.dto.ReviewDTO;
import com.prac.dto.ReviewDTO.ReviewByUserDTO;
import com.prac.dto.ReviewDTO.ReviewIntegrationDTO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.prac.domain.QUser.user;
import static com.prac.domain.joinTable.QReview.review;

@RequiredArgsConstructor
@Transactional
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public ReviewByUserDTO findReviewsByUser(Long userId) {


        List<Tuple> tuples = queryFactory
                .select(user, review)
                .from(user)
                .leftJoin(review).on(review.user.eq(user))
                .where(user.id.eq(userId))
                .fetch();

        User user = tuples.get(0).get(QUser.user);
        List<Review> reviews = tuples.stream().map(t -> t.get(review)).collect(Collectors.toList());

        if(reviews.stream().allMatch(r -> r == null)) {
            return new ReviewByUserDTO(user);
        }

        return new ReviewByUserDTO(user, reviews);
    }

}
