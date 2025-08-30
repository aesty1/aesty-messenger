package ru.denis.aestymes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.denis.aestymes.models.MyUser;
import ru.denis.aestymes.repositories.MyUserRepository;

import java.util.List;

@Service
public class MyUserService implements UserDetailsService {

    @Autowired
    private MyUserRepository myUserRepository;

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
}
