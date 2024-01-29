package com.sparta.secureschedulerappserver.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Size(min = 4, max = 10, message = "4자 이상 10자 이하로 입력하세요.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "알파벳 소문자, 숫자로만 구성되어야 합니다.")
    @Column(nullable = false, unique = true, length = 10)
    private String username;

    @Size(min = 8, max = 15, message = "8자 이상 15자 이하로 입력하세요.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "알파벳 소문자, 숫자로만 구성되어야 합니다.")
    @Column(nullable = false, unique = true, length = 15)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Schedule> schedules = new ArrayList<>();
}
