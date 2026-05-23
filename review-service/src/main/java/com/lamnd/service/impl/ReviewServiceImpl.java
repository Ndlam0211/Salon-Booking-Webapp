package com.lamnd.service.impl;

import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.ReviewRequest;
import com.lamnd.entity.Review;
import com.lamnd.repository.ReviewRepo;
import com.lamnd.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;

    @Override
    public Review createReview(ReviewRequest reviewRequest, UserDTO user, SalonDTO salon) {
        Review review = Review.builder()
                .reviewContent(reviewRequest.reviewContent())
                .rating(reviewRequest.rating())
                .salonId(salon.id())
                .userId(user.id())
                .build();

        return reviewRepo.save(review);
    }

    @Override
    public List<Review> getReviewsBySalonId(Long salonId) {
        return reviewRepo.findBySalonId(salonId);
    }

    @Override
    public Review updateReview(ReviewRequest reviewRequest, Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);

        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to update this review");
        }

        review.setReviewContent(reviewRequest.reviewContent());
        review.setRating(reviewRequest.rating());

        return reviewRepo.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);

        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to update this review");
        }

        reviewRepo.delete(review);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }
}
