package com.sparta.secureschedulerappserver.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sparta.secureschedulerappserver.config.WebSecurityConfig;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.exception.NotFoundCommentException;
import com.sparta.secureschedulerappserver.exception.NotFoundScheduleException;
import com.sparta.secureschedulerappserver.exception.NotFoundUserException;
import com.sparta.secureschedulerappserver.jwt.JwtUtil;
import com.sparta.secureschedulerappserver.redis.RefreshTokenRedisRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.CommentServiceImpl;
import com.sparta.secureschedulerappserver.service.ScheduleServiceImpl;
import com.sparta.secureschedulerappserver.service.UserServiceImpl;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(controllers = {ScheduleErrorController.class, UserController.class,
    ScheduleController.class, CommentController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    })
public class RestControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private ScheduleServiceImpl scheduleServiceImpl;

    @MockBean
    private CommentServiceImpl commentServiceImpl;

    @MockBean
    private JwtUtil jwtUtil;

ㄱ
    @MockBean
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Autowired
    private WebApplicationContext context;


    private Principal mockPrincipal;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilters(new MockSpringSecurityFilter())
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "tester";
        String password = "password";
        User testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "",
            testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("유저를 찾을 수 없습니다.")
    void notFoundUser() throws Exception {
        mockUserSetup();
        when(scheduleServiceImpl.getSchedulesForUser(any(), any())).thenThrow(
            new NotFoundUserException()
        );

        mockMvc.perform(get("/schedules/mySchedules")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isBadRequest())
            .andDo(print());

    }

    @Test
    @DisplayName("스케쥴을 찾을 수 없습니다.")
    void notFoundSchedule() throws Exception {
        mockUserSetup();
        when(scheduleServiceImpl.getSchedulesForUser(any(), any())).thenThrow(
            new NotFoundScheduleException()
        );

        mockMvc.perform(get("/schedules/mySchedules")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isBadRequest())
            .andDo(print());

    }

    @Test
    @DisplayName("댓글을 찾을 수 없습니다.")
    void notFoundComment() throws Exception {
        mockUserSetup();
        when(commentServiceImpl.updateComment(anyLong(), any(), any())).thenThrow(
            new NotFoundCommentException()
        );

        mockMvc.perform(patch("/schedules/{scheduleId}/comments/{commentId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }
}
