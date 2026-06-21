package com.ice.springai.chat.record;

public record BookReview (
        String reviewerName,
        int rating,
        String review
){
}
