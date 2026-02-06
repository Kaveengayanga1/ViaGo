package com.viago.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private Long raterUserId;
    private Long ratedUserId;
    private LocalDateTime createdAt;
}
