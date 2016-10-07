package com.azumo.assignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TwitterController {

	@Autowired
	private Twitter twitter;
	@Autowired
	private TweetRepository tweetRepo;

	private ConnectionRepository connectionRepository;

	@Inject
	public TwitterController(Twitter twitter, ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}

	@RequestMapping("/login")
	public String login(Model model) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        model.addAttribute(twitter.userOperations().getUserProfile());
        return "greeting";
	}
	
	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	@RequestMapping("/search")
	public String search(@RequestParam(value = "q", required = true) String q, Model model) {
		model.addAttribute("q", q);
		List<String> tweets = new ArrayList<String>();
		Iterator<Tweet> iterator = twitter.searchOperations().search(q, 15).getTweets().iterator();
		while (iterator.hasNext()) {
			Tweet t = iterator.next();
			tweets.add(t.getText());
			tweetRepo.save(new MyTweet(t.getText(), t.getCreatedAt(), t.getFromUser()));
		}
		model.addAttribute("tweets", tweets);

		return "search";
	}

	@RequestMapping("/tweets-from-db")
	public String tweetsFromDB(Model model) {
		List<MyTweet> tweets = new ArrayList<MyTweet>();
		long count = tweetRepo.count();
		Sort sort = new Sort(Direction.DESC, "createdAt");
		for (MyTweet tweet : tweetRepo.findAll(sort)) {
			tweets.add(tweet);
		}
		model.addAttribute("count", count);
		model.addAttribute("tweets", tweets);
		return "fromdb";
	}
	
	
	
	@RequestMapping("*")
	public String page404(){
		return "404";
	}
	
}
