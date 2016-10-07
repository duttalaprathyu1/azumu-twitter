package com.azumo.assignment;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.social.twitter.api.Tweet;

public interface TweetRepository extends MongoRepository<MyTweet, String> {
	// custom search
	List<MyTweet> findByText(String text);
}
