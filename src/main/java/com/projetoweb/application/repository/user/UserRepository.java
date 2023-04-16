package com.projetoweb.application.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projetoweb.application.model.user.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
