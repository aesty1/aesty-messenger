package ru.denis.aestymes.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.denis.aestymes.dtos.ChangePasswordDto;
import ru.denis.aestymes.dtos.UserDTO;
import ru.denis.aestymes.jwts.JwtProvider;
import ru.denis.aestymes.models.MyUser;
import ru.denis.aestymes.repositories.MyUserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MyUserService implements UserDetailsService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void save(MyUser myUser) {
        String confirmationToken = UUID.randomUUID().toString();

        myUser.setConfirmationToken(confirmationToken);

        myUserRepository.save(myUser);
        emailService.sendConfirmationEmail(myUser.getEmail(), confirmationToken);
    }

    public MyUser getMyUser(Long id) {
        return myUserRepository.getReferenceById(id);
    }

    public List<MyUser> getAllMyUsers() {
        return myUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MyUser user = myUserRepository.findMyUserByEmail(email);

        if(user != null) {
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPasswordHash())
                    .build();

        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    public MyUser getUserByEmail(String email) {
        return myUserRepository.findMyUserByEmail(email);
    }

    public Long getCurrentUserId(HttpServletRequest request) {
        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                .filter(cook -> cook.getName().equals("JWT_TOKEN"))
                .findFirst();

        if(cookie != null || cookie.isPresent()) {
            String email = jwtProvider.extractUsername(cookie.get().getValue());

            return getUserByEmail(email).getId();
        }

        return -1L;
    }

    public MyUser getUserById(Long id) {
        return myUserRepository.getMyUserById(id);
    }

    public void updateProfile(Long user_id, UserDTO dto) {
        MyUser user = myUserRepository.getMyUserById(user_id);

        if(user != null) {
            user.setName(dto.getName());
            user.setUsername(dto.getUsername());
            user.setAvatarUrl(dto.getAvatarUrl());

            myUserRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void changePassword(ChangePasswordDto passwordDto, MyUser user) {
        if(!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Wrong old password");
        }

        if(!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            throw new BadCredentialsException("Confirm password is not the same");
        }

        user.setPasswordHash(passwordEncoder.encode(passwordDto.getNewPassword()));

        myUserRepository.save(user);
    }

    public boolean confirmUser(String confirmationToken) {
        MyUser user = myUserRepository.findByConfirmationToken(confirmationToken);

        if(user != null) {
            user.setVerified(true);
            user.setConfirmationToken(null);

            myUserRepository.save(user);

            return true;
        } else {
            return false;
        }
    }
}
