package com.prac.repository.user;

import com.prac.config.BasicConfiguration;
import com.prac.domain.User;
import com.prac.domain.etc.Address;
import com.prac.domain.etc.Gender;
import com.prac.domain.etc.Role;
import com.prac.dto.UserDTO;
import com.prac.dto.UserDTO.UserDetailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BasicConfiguration.class)
class UserRepositoryImplTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .address(new Address("postcode", "road", "jibun", "detail", "extra"))
                .name("user1")
                .email("user1@naver.com")
                .gender(Gender.MALE)
                .username("user1id")
                .password("user1pwd")
                .nickname("nickname")
                .role(Role.ROLE_USER)
                .profileImg("profileImg")
                .build();

        em.persist(user);
    }

    @Test
    void findUserDetailDTO() {
        UserDetailDTO userDetailDTO = userRepository.findUserDetailDTO(user.getId());
        assertThat(userDetailDTO).isEqualTo(new UserDetailDTO(user));
    }

}