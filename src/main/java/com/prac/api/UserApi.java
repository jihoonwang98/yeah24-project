package com.prac.api;

import com.prac.dto.UserDTO.UserModifyDTO;
import com.prac.service.UserService;
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
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApi {

    private final UserService userService;


    @PatchMapping("/{id}")
    public ResponseEntity<?> modify(@PathVariable("id") Long id, @Valid UserModifyDTO modifyDTO, BindingResult bindingResult) {
        log.info("id: " + id);
        log.info("modify DTO: " + modifyDTO);

        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String field = fieldError.getField();
                Object rejectedValue = fieldError.getRejectedValue();
                String defaultMessage = fieldError.getDefaultMessage();
                boolean bindingFailure = fieldError.isBindingFailure();

                log.info("error codes: ");
                for (String code : fieldError.getCodes()) {
                    log.info("\t" + code);
                }

                log.info("field = " + field);
                log.info("rejectedValue = " + rejectedValue);
                log.info("defaultMessage = " + defaultMessage);
                log.info("bindingFailure = " + bindingFailure);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrors);
        }

        if (modifyDTO.getProfileImg() != null && !modifyDTO.getProfileImg().getContentType().startsWith("image")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지 파일만 처리 가능");
        }

        userService.modifyUser(id, modifyDTO);

        return ResponseEntity.ok("Modify Success");
    }


    @PostMapping("/{id}/wishlist")
    public ResponseEntity<String> addToWishlist(@PathVariable("id") Long userId, @RequestBody List<Long> bookIds) {
        log.info("bookIds :" + bookIds);
        userService.addBookToWishList(userId, bookIds);
        return ResponseEntity.ok("Added To WishList.");
    }

    @DeleteMapping("/{id}/wishlist")
    public ResponseEntity<String> deleteInWishlist(@PathVariable("id") Long userId, @RequestBody List<Long> bookIds) {
        log.info("bookIds: " + bookIds);
        userService.deleteBooksInWishList(userId, bookIds);
        return ResponseEntity.ok("Deleted In WishList.");
    }
}
