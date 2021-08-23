package com.prac.api;

import com.prac.dto.ReviewDTO;
import com.prac.dto.ReviewDTO.ReviewDeleteDTO;
import com.prac.dto.ReviewDTO.ReviewPostDTO;
import com.prac.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reviews")
public class ReviewApi {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> postReview(@RequestBody @Valid ReviewPostDTO postDTO, BindingResult bindingResult) {
        log.info("POST /api/reviews");
        log.info("postDTO: " + postDTO);

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrors);
        }

        reviewService.post(postDTO);
        return ResponseEntity.ok("리뷰 작성 완료");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteReview(@RequestBody ReviewDeleteDTO deleteDTO) {
        log.info("DELETE /api/reviews");
        log.info("deleteDTO: " + deleteDTO);

        reviewService.delete(deleteDTO);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }

}
