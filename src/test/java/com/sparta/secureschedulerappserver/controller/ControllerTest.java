package com.sparta.secureschedulerappserver.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.secureschedulerappserver.config.WebSecurityConfig;
import com.sparta.secureschedulerappserver.dto.CommentDeleteDto;
import com.sparta.secureschedulerappserver.dto.CommentRequestDto;
import com.sparta.secureschedulerappserver.dto.CommentResponseDto;
import com.sparta.secureschedulerappserver.dto.PageDto;
import com.sparta.secureschedulerappserver.dto.ScheduleListResponseDto;
import com.sparta.secureschedulerappserver.dto.ScheduleRequestDto;
import com.sparta.secureschedulerappserver.dto.ScheduleResponseDto;
import com.sparta.secureschedulerappserver.dto.UserRequestDto;
import com.sparta.secureschedulerappserver.entity.Comment;
import com.sparta.secureschedulerappserver.entity.Schedule;
import com.sparta.secureschedulerappserver.entity.User;
import com.sparta.secureschedulerappserver.jwt.JwtUtil;
import com.sparta.secureschedulerappserver.redis.RefreshTokenRedisRepository;
import com.sparta.secureschedulerappserver.security.UserDetailsImpl;
import com.sparta.secureschedulerappserver.service.CommentServiceImpl;
import com.sparta.secureschedulerappserver.service.ScheduleServiceImpl;
import com.sparta.secureschedulerappserver.service.UserServiceImpl;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(
    controllers = {UserController.class, ScheduleController.class, CommentController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
@ActiveProfiles("test")
public class ControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;


    @MockBean
    private RefreshTokenRedisRepository refreshTokenRedisRepository;

    @MockBean
    CommentServiceImpl commentService;

    @MockBean
    ScheduleServiceImpl scheduleService;

    @MockBean
    UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
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


    @Nested
    @DisplayName("UserController 테스트")
    class userControllerTest {

        @Test
        @DisplayName("회원 가입 처리")
        void testJoin() throws Exception {
            // given
            UserRequestDto userRequestDto = new UserRequestDto("soller", "robbie1234");

            // when
            mvc.perform(post("/user/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 성공적으로 완료되었습니다."))
                .andExpect(jsonPath("$.httpStatus").value("OK"));

            // then
            verify(userService).join(any(UserRequestDto.class));
        }
    }

    @Nested
    @DisplayName("ScheduleController 테스트")
    class scheduleControllerTest {

        @Test
        @DisplayName("게시글 생성 테스트")
        public void testCreateSchedule() throws Exception {
            // 유효한 ScheduleRequestDto를 생성
            mockUserSetup();
            ScheduleRequestDto requestDto = new ScheduleRequestDto("testTitle", "testContent");
            // requestDto 설정...

            // POST 요청 보내기
            mvc.perform(post("/schedules")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .accept(MediaType.APPLICATION_JSON)
                    .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
        }

        @Test
        @DisplayName("게시글 1개 조회 테스트")
        public void testReadSchedule() throws Exception {
            // given
            Long scheduleId = 1L;
            User user = new User("testUser", "testPassword");
            Schedule schedule = new Schedule("title", "content", user);

            // ScheduleResponseDto를 생성합니다. 이 객체는 테스트에서 반환되어야 하는 응답입니다.
            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            // responseDto 설정...

            // when
            // scheduleService의 readSchedule 메서드가 scheduleId로 호출되면 responseDto를 반환하도록 설정
            when(scheduleService.readSchedule(scheduleId)).thenReturn(responseDto);

            // then
            // GET 요청을 보내고, 상태 코드가 200이며, 반환되는 JSON이 responseDto와 일치하는지 확인합니다.
            mvc.perform(get("/schedules/" + scheduleId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(print());
        }

        @Test
        @DisplayName("스케쥴 전체 조회")
        @WithMockUser(username = "testUser", password = "testPassword")
        public void testReadSchedules() throws Exception {
            // 필요한 ScheduleResponseDto 리스트를 생성합니다.
            List<ScheduleResponseDto> scheduleList = new ArrayList<>();
            // scheduleList 설정...
            User user1 = new User("fakeuser1", "password");

            Schedule test1 = new Schedule("title1", "content1", user1);
            Schedule test2 = new Schedule("title2", "content1", user1);
            scheduleList.add(new ScheduleResponseDto(test1));
            scheduleList.add(new ScheduleResponseDto(test2));

            // ScheduleListResponseDto를 생성합니다. 이 객체는 테스트에서 반환되어야 하는 응답입니다.
            // 필요한 매개변수를 제공하여 객체를 생성합니다.
            Map<String, List<ScheduleResponseDto>> scheduleMap = new HashMap<>();
            scheduleMap.put("testKey", scheduleList);
            ScheduleListResponseDto listResponseDto = new ScheduleListResponseDto(scheduleMap);

            // scheduleService의 readSchedules 메서드가 호출되면 listResponseDto를 반환하도록 설정
            when(scheduleService.readSchedules()).thenReturn(listResponseDto);

            // GET 요청을 보내고, 상태 코드가 200이며, 반환되는 JSON이 listResponseDto와 일치하는지 확인합니다.
            mvc.perform(get("/schedules")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listResponseDto)))
                .andDo(print());
        }

        @Test
        @DisplayName("자신 스케쥴 조회")
        @WithMockUser(username = "testUser", password = "testPassword")
        public void testReadMySchedules() throws Exception {
            // given
            mockUserSetup();
            User testUser = new User("testUser", "testPassword");
            UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
            Schedule schedule1 = new Schedule("title1", "content1", testUser);
            Schedule schedule2 = new Schedule("title2", "content1", testUser);

            // scheduleList 설정...
            List<ScheduleResponseDto> scheduleList = new ArrayList<>();
            scheduleList.add(new ScheduleResponseDto(schedule1));
            scheduleList.add(new ScheduleResponseDto(schedule2));

            PageDto pageDto = PageDto.builder().currentPage(1).size(2).build();

            // UserRepository와 ScheduleRepository에서 테스트에 필요한 값을 반환하도록 설정
            when(scheduleService.getSchedulesForUser(testUserDetails, pageDto)).thenReturn(
                scheduleList);

            // when & then
            mvc.perform(get("/schedules/mySchedules")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
        }


        @Test
        public void testReadUncompleteSchedule() throws Exception {
            // 필요한 ScheduleListResponseDto를 생성합니다.
            List<ScheduleResponseDto> scheduleList = new ArrayList<>();
            // scheduleList 설정...

            Map<String, List<ScheduleResponseDto>> scheduleMap = new HashMap<>();
            scheduleMap.put("testKey", scheduleList);
            ScheduleListResponseDto listResponseDto = new ScheduleListResponseDto(scheduleMap);

            // scheduleService의 readUncompleteSchedules 메서드가 호출되면 listResponseDto를 반환하도록 설정
            given(scheduleService.readUncompleteSchedules()).willReturn(listResponseDto);

            // GET 요청을 보내고, 상태 코드가 200이며, 반환되는 JSON이 listResponseDto와 일치하는지 확인합니다.
            mvc.perform(get("/schedules/uncompleted")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listResponseDto)))
                .andDo(print());
        }

        @Test
        public void testFindSchedule() throws Exception {
            // 필요한 ScheduleResponseDto 리스트를 생성합니다.
            List<ScheduleResponseDto> scheduleList = new ArrayList<>();
            // scheduleList 설정...

            // scheduleService의 findSchedules 메서드가 호출되면 scheduleList를 반환하도록 설정
            given(scheduleService.findSchedules("test")).willReturn(scheduleList);

            // GET 요청을 보내고, 상태 코드가 200이며, 반환되는 JSON이 scheduleList와 일치하는지 확인합니다.
            mvc.perform(get("/schedules/search")
                    .param("text", "test")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(scheduleList)))
                .andDo(print());
        }


    }

    @Nested
    @DisplayName("CommentController 테스트")
    class commentControllerTest {

        @Test
        //@WithMockUser(username = "testUser", password = "testPassword")
        public void testCreateComment() throws Exception {
            // given
            mockUserSetup();
            CommentRequestDto commentRequestDto = new CommentRequestDto("comment");

            User user = new User("testUser", "testPassword");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Schedule schedule = new Schedule("title", "content", user);
            schedule.setScheduleId(1L);
            Comment comment = new Comment("comment", schedule, user);
            comment.setCommentId(1L);
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

            given(commentService.createComment(eq(1L), eq(commentRequestDto),
                eq(userDetails))).willReturn(commentResponseDto);

            // when
            var action = mvc.perform(post("/schedules/{scheduleId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequestDto))
                .principal(mockPrincipal));

            // then
            action.andExpect(status().isOk())
                .andDo(print());
        }

        @Test
        @WithMockUser(username = "testUser", password = "testPassword")
        public void testUpdateComment() throws Exception {
            // given
            mockUserSetup();
            CommentRequestDto updatedCommentRequestDto = new CommentRequestDto("updated comment");

            User user = new User("testUser", "testPassword");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Schedule schedule = new Schedule("title", "content", user);
            schedule.setScheduleId(1L);
            Comment comment = new Comment("comment", schedule, user);
            comment.setCommentId(1L);
            CommentResponseDto updatedCommentResponseDto = new CommentResponseDto(comment);

            given(commentService.updateComment(eq(1L), eq(updatedCommentRequestDto),
                eq(userDetails))).willReturn(updatedCommentResponseDto);

            // when
            var action = mvc.perform(patch("/schedules/{scheduleId}/comments/{commentId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCommentRequestDto))
                .principal(mockPrincipal));

            // then
            action.andExpect(status().isOk())
                .andDo(print());
        }

        @Test
        @WithMockUser(username = "testUser", password = "testPassword")
        public void testDeleteComment() throws Exception {
            // given
            mockUserSetup();

            User user = new User("testUser", "testPassword");
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            CommentDeleteDto deleteResponse = new CommentDeleteDto("삭제가 정상적으로 처리되었습니다.",
                HttpStatus.OK);

            // when
            var action = mvc.perform(delete("/schedules/{scheduleId}/comments/{commentId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal));

            // then
            action.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(deleteResponse.getMessage()))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andDo(print());
        }


    }


}
