package com.sparta.secureschedulerappserver.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageDtoTest {

    @Test
    @DisplayName("Test PageDto Constructor")
    public void whenSortByIsNull_thenCreatePageableWithoutSort(){
        PageDto dto = PageDto.builder()
            .currentPage(1)
            .size(10)
            .build();

        Pageable pageable = dto.toPageable();

        assertThat(pageable).isEqualTo(PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("정렬 null일 경우 Pageable 생성 여부")
    public void whenSortByIsNotNull_thenCreatePageableWithSort(){
        PageDto dto = PageDto.builder()
            .currentPage(1)
            .size(10)
            .sortBy("name")
            .build();

        Pageable pageable = dto.toPageable();

        assertThat(pageable.getSort()).isEqualTo(Sort.by("name").descending());
        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(10);
    }
}
