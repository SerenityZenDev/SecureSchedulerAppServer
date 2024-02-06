package com.sparta.secureschedulerappserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "SCHEDULE")
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(nullable = false, length = 64)
    private String title;

    @Column(nullable = false, length = 1024)
    private String content;

    @Column(updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private boolean isCompleted;

    @Column(nullable = false)
    private boolean hidden;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "scheduleId")
    private List<Comment> comments = new ArrayList<>();

    public Schedule(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createAt = LocalDateTime.now();
        this.isCompleted = false;
        this.hidden = false;
    }


    public void update(ScheduleRequestDto scheduleRequestDto) {
        this.title = scheduleRequestDto.getTitle();
        this.content = scheduleRequestDto.getContent();
    }

    public void complete() {
        this.isCompleted = true;
    }

    public void optionHidden(){this.hidden = true;}
}
