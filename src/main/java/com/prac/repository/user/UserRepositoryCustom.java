package com.prac.repository.user;

import com.prac.dto.UserDTO;
import com.prac.dto.UserDTO.UserDetailDTO;

public interface UserRepositoryCustom {

    UserDetailDTO findUserDetailDTO(Long id);

}
