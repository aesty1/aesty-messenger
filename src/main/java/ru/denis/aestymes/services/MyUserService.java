package ru.denis.aestymes.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.denis.aestymes.jwts.JwtProvider;
import ru.denis.aestymes.models.MyUser;
import ru.denis.aestymes.repositories.MyUserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserService implements UserDetailsService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private JwtProvider jwtProvider;

    public void save(MyUser myUser) {
        myUserRepository.save(myUser);
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
}
