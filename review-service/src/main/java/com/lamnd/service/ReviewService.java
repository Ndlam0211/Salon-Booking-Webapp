package com.lamnd.service;

import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.ReviewRequest;
import com.lamnd.entity.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(ReviewRequest reviewRequest,
                        UserDTO user,
                        SalonDTO salon);

    List<Review> getReviewsBySalonId(Long salonId);

    Review updateReview(ReviewRequest reviewRequest,
                        Long reviewId,
                        Long userId);

    void deleteReview(Long reviewId, Long userId);
}
