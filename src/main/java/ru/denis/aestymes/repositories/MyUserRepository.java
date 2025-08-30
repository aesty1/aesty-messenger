package ru.denis.aestymes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.aestymes.models.MyUser;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByEmail(String email);

    MyUser findMyUserByEmail(String email);
}
