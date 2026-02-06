package com.viago.service.controller;

import com.viago.service.dto.request.SubmitRatingRequest;
import com.viago.service.dto.response.ReviewDTO;
import com.viago.service.dto.response.UserResponse;
import com.viago.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@CrossOrigin
@RequiredArgsConstructor
public class ReviewController {

    private final UserService userService;

    @PostMapping
    public UserResponse<ReviewDTO> submitRating(
            @RequestParam Long raterUserId,
            @Valid @RequestBody SubmitRatingRequest request) {
        return userService.submitRating(raterUserId, request);
    }
}
