package com.prac.repository.review;

import com.prac.dto.ReviewDTO;
import com.prac.dto.ReviewDTO.ReviewByUserDTO;
import com.prac.dto.ReviewDTO.ReviewIntegrationDTO;

public interface ReviewRepositoryCustom {

    ReviewByUserDTO findReviewsByUser(Long userId);

}
