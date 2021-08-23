package com.prac.service;

import com.prac.domain.Book;
import com.prac.domain.User;
import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.BookWishlistDTO;
import com.prac.dto.BookDTO.BookWishlistResponse;
import com.prac.dto.UserDTO.UserDetailDTO;
import com.prac.dto.UserDTO.UserModifyDTO;
import com.prac.dto.UserDTO.UserRegisterDTO;

import com.prac.repository.book.BookRepository;
import com.prac.repository.user.UserRepository;
import com.prac.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final S3UploadService uploadService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(UserRegisterDTO registerDTO) {
        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        MultipartFile profileImg = registerDTO.getProfileImg();
        String imgSrc = null;

        if(!profileImg.isEmpty()) {
            imgSrc = getImgSrc(profileImg);
            uploadImage(profileImg, imgSrc);
        }

        User user = registerDTO.toEntity(imgSrc);

        userRepository.save(user);
        return user.getId();
    }

    public UserDetailDTO findUserDetailDTO(Long id) {
        return userRepository.findUserDetailDTO(id);
    }


    public void modifyUser(Long id, UserModifyDTO modifyDTO) {
        MultipartFile profileImg = modifyDTO.getProfileImg();

        String imgSrc = null;

        if (profileImg != null) {
            imgSrc = getImgSrc(profileImg);
            uploadImage(profileImg, imgSrc);
        }

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());

        user.modify(
                passwordEncoder.encode(modifyDTO.getPassword()),
                modifyDTO.getNickname(),
                imgSrc,
                modifyDTO.getAddress()
        );
    }


    public void addBookToWishList(Long userId, List<Long> bookIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        List<Book> books = bookRepository.findAllById(bookIds);

        user.addToWishlist(books);
    }


    public void deleteBooksInWishList(Long userId, List<Long> bookIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        List<Book> books = bookRepository.findAllById(bookIds);

        books.forEach(b -> user.deleteInWishlist(b));
    }


    public BookWishlistResponse findBookWishlistResponse(Long userId) {
        return bookRepository.findBookWishlistResponse(userId);
    }


    private void uploadImage(MultipartFile profileImg, String imgSrc) {
        try {
            uploadService.upload(profileImg, imgSrc);
        } catch (IOException e) {
            log.error("IOException in register", e);
        }
    }

    private String getImgSrc(MultipartFile profileImg) {
        String folderPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).replace("/", File.separator);
        String uuid = UUID.randomUUID().toString();
        String fileName = profileImg.getOriginalFilename();
        return folderPath + File.separator + uuid + "_" + fileName;
    }
}
