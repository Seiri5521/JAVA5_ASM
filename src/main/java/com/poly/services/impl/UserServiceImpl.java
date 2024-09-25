package com.poly.services.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.dto.ChangePasswordDTO;
import com.poly.dto.LoginDTO;
import com.poly.dto.RegisterDTO;
import com.poly.dto.UpdateUserDTO;
import com.poly.dto.UserDTO;
import com.poly.model.User;
import com.poly.repository.BookingRepository;
import com.poly.repository.UserRepository;
import com.poly.services.UserService;
import com.poly.utilites.ConvertUserToDto;
import com.poly.utilites.SessionUtilities;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Sử dụng BCryptPasswordEncoder của Spring Security
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Page<UserDTO> findAllUser(String sdt, String email, String ho_ten, Pageable pageable) {
        Page<User> page = userRepository.findAll(sdt, email, ho_ten, pageable);

        Page<UserDTO> pageUserDTO = new PageImpl<>(
            page.getContent().stream().map(user -> ConvertUserToDto.convertUsertoDto(user))
                .collect(Collectors.toList()),
            page.getPageable(),
            page.getTotalElements()
        );

        return pageUserDTO;
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean saveUser(User user) {
        return userRepository.save(user) != null;
    }

    @Override
    public boolean updateUser(UpdateUserDTO updateUserDTO) {
        if (SessionUtilities.getUser() != null) {
            Long userId = SessionUtilities.getUser().getId();

            User user = userRepository.findById(userId).orElse(null);
            if (user == null) return false;

            user.setSdt(updateUserDTO.getSdt());
            user.setUsername(updateUserDTO.getUsername());
            user.setEmail(updateUserDTO.getEmail());
            user.setDia_chi(updateUserDTO.getDia_chi());
            user.setHo_ten(updateUserDTO.getHo_ten());
            user.setGioi_tinh(updateUserDTO.getGioi_tinh());

            userRepository.save(user);
            SessionUtilities.setUser(ConvertUserToDto.convertUsertoDto(user));

            return true;
        }

        return false;
    }

    @Override
    public boolean deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && (bookingRepository.findBookingByUserId(id) == null || bookingRepository.findBookingByUserId(id).isEmpty())) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean login(LoginDTO user) {
        User userCheck = findUserByUsername(user.getUsername());

        if (userCheck == null) {
            return false;
        }

        log.info("userCheck:{}", userCheck.getUsername());

        // Sử dụng BCryptPasswordEncoder để kiểm tra mật khẩu
        if (passwordEncoder.matches(user.getPassword(), userCheck.getPassword())) {
            SessionUtilities.setUsername(userCheck.getUsername());
            SessionUtilities.setUser(ConvertUserToDto.convertUsertoDto(userCheck));

            log.info("userCheck:{}", SessionUtilities.getUsername());
            return true;
        }

        return false;
    }

    @Override
    public boolean register(RegisterDTO newUser) {
        User userCheckByUserName = findUserByUsername(newUser.getUsername());
        User userCheckByEmail = userRepository.getUserByEmail(newUser.getEmail());

        if (userCheckByUserName != null || userCheckByEmail != null) {
            return false;
        }

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setHo_ten(newUser.getHo_ten());
        user.setPassword(passwordEncoder.encode(newUser.getPassword())); // Mã hóa mật khẩu
        user.setEmail(newUser.getEmail());
        user.setGioi_tinh(newUser.getGioi_tinh());
        user.setRole(1);
        user.setDia_chi(newUser.getDia_chi());
        user.setSdt(newUser.getSdt());

        return saveUser(user);
    }

    @Override
    public boolean checkLogin() {
        return SessionUtilities.getUsername() != null;
    }

    @Override
    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        if (SessionUtilities.getUser() != null) {
            Long userId = SessionUtilities.getUser().getId();

            User user = userRepository.findById(userId).orElse(null);
            if (user == null) return false;

            // Kiểm tra và mã hóa mật khẩu mới
            if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword()) && changePasswordDTO.getNewPassword() != null) {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean updateUserByAdmin(UpdateUserDTO updateUserDTO, Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setSdt(updateUserDTO.getSdt());
            user.setUsername(updateUserDTO.getUsername());
            user.setEmail(updateUserDTO.getEmail());
            user.setDia_chi(updateUserDTO.getDia_chi());
            user.setHo_ten(updateUserDTO.getHo_ten());
            user.setGioi_tinh(updateUserDTO.getGioi_tinh());

            userRepository.save(user);

            return true;
        }

        return false;
    }

    @Override
    public boolean adminLogin(LoginDTO user) {
        User userCheck = findUserByUsername(user.getUsername());

        if (userCheck == null) {
            return false;
        }

        log.info("userCheck:{}", userCheck.getUsername());

        // Kiểm tra mật khẩu và quyền admin
        if (passwordEncoder.matches(user.getPassword(), userCheck.getPassword()) && userCheck.getRole() == 0) {
            SessionUtilities.setAdmin(ConvertUserToDto.convertUsertoDto(userCheck));

            log.info("userCheck:{}", SessionUtilities.getAdmin().getUsername());

            return true;
        }

        return false;
    }

    @Override
    public boolean checkAdminLogin() {
        return SessionUtilities.getAdmin() != null;
    }

    @Override
    public void adminLogout() {
        SessionUtilities.setAdmin(null);
    }

    @Override
    public boolean resetPass(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return false;

        // Đặt lại mật khẩu mặc định
        user.setPassword(passwordEncoder.encode("123@123a"));

        return saveUser(user);
    }
}
