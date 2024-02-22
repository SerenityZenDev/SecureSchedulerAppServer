package com.sparta.secureschedulerappserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank
    private String comment;

    public CommentRequestDto(String comment) {
        this.comment = comment;
    }
}
