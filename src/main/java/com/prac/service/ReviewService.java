package com.prac.service;

import com.prac.domain.Book;
import com.prac.domain.User;
import com.prac.domain.etc.ReviewKey;
import com.prac.domain.joinTable.Review;
import com.prac.dto.ReviewDTO;
import com.prac.dto.ReviewDTO.ReviewByUserDTO;
import com.prac.dto.ReviewDTO.ReviewDeleteDTO;
import com.prac.dto.ReviewDTO.ReviewIntegrationDTO;
import com.prac.dto.ReviewDTO.ReviewPostDTO;
import com.prac.repository.book.BookRepository;
import com.prac.repository.review.ReviewRepository;
import com.prac.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void post(ReviewPostDTO postDTO) {

        User user = userRepository.findById(postDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException());
        Book book = bookRepository.findById(postDTO.getBookId()).orElseThrow(() -> new EntityNotFoundException());

        Review review = postDTO.toEntity(user, book);
        reviewRepository.save(review);
    }


    public ReviewByUserDTO findReviewsByUser(Long userId) {
        return reviewRepository.findReviewsByUser(userId);
    }

    public void delete(ReviewDeleteDTO deleteDTO) {
        reviewRepository.deleteById(new ReviewKey(deleteDTO.getUserId(), deleteDTO.getBookId()));
    }
}
