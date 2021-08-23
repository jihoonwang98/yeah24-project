package com.prac.repository.review;

import com.prac.domain.etc.ReviewKey;
import com.prac.domain.joinTable.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, ReviewKey>, ReviewRepositoryCustom {
}
