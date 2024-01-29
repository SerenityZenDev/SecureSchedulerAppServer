package com.sparta.secureschedulerappserver.repository;

import com.sparta.secureschedulerappserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
