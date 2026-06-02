package com.lamnd.mapper;

import com.lamnd.dto.ReviewDTO;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.entity.Review;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {

    public static ReviewDTO toDTO(Review review, UserDTO user) {
        return ReviewDTO.builder()
                .id(review.getId())
                .reviewContent(review.getReviewContent())
                .rating(review.getRating())
                .user(user)
                .createdAt(review.getCreatedAt().toLocalDate())
                .build();
    }

    public static List<ReviewDTO> toDTOList(List<Review> reviews, UserDTO user) {
        return reviews.stream()
                .map(review -> toDTO(review, user))
                .collect(Collectors.toList());
    }
}
