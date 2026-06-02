package com.lamnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.dto.ReviewDTO;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.ReviewRequest;
import com.lamnd.entity.Review;
import com.lamnd.mapper.ReviewMapper;
import com.lamnd.service.ReviewService;
import com.lamnd.service.client.SalonFeignClient;
import com.lamnd.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserFeignClient userFeignClient;
    private final SalonFeignClient salonFeignClient;
    private final ObjectMapper objectMapper;

    @PostMapping("/salon/{salonId}")
    public ResponseEntity<Review> createReview(
            @RequestBody ReviewRequest reviewRequest,
            @PathVariable("salonId") Long salonId,
            @RequestHeader("Authorization") String token) {

        UserDTO userDTO = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        SalonDTO salonDTO = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonById(salonId).getBody()).data(), SalonDTO.class);

        Review review = reviewService.createReview(reviewRequest, userDTO, salonDTO);

        return ResponseEntity.ok(review);
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsBySalonId(
            @PathVariable("salonId") Long salonId,
            @RequestHeader("Authorization") String token) {

        List<Review> reviews = reviewService.getReviewsBySalonId(salonId);

        List<ReviewDTO> reviewDTOs = reviews.stream().map((review) -> {
            UserDTO user = null;
            try {
                user = objectMapper
                        .convertValue(Objects.requireNonNull(userFeignClient.getUserById(review.getUserId()).getBody()).data(), UserDTO.class);
            }
            catch (Exception e) {
               throw new RuntimeException("Failed to fetch user info for review with ID: " + review.getId(), e);
            }
            return ReviewMapper.toDTO(review, user);
        }).toList();

        return ResponseEntity.ok(reviewDTOs);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody ReviewRequest reviewRequest,
            @PathVariable("reviewId") Long reviewId,
            @RequestHeader("Authorization") String token) {

        UserDTO userDTO = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        Review review = reviewService.updateReview(reviewRequest, reviewId, userDTO.id());

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestHeader("Authorization") String token) {

        UserDTO userDTO = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        reviewService.deleteReview(reviewId, userDTO.id());

        return ResponseEntity.noContent().build();
    }
}
