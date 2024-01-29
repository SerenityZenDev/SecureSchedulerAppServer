package com.sparta.secureschedulerappserver.repository;

import com.sparta.secureschedulerappserver.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
