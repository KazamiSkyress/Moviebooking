package com.example.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.ErrorResponse;
import com.example.movie.dto.Reply;

import com.example.movie.entity.Movie;
import com.example.movie.exception.InvalidUsernameException;
import com.example.movie.exception.MovieNotFoundException;
import com.example.movie.services.MovieService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Bhakti Joshi
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Slf4j
public class MovieController {

//	Injected TweetService bean
	@Autowired
	private MovieService movieService;

//  Kafka Configuration
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
//	Kafka Topic Name
	private static final String KAFKA_TOPIC = "movies";

	/**
	 * Controller Method to get all tweets HTTP GET Request
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/all
	 */
	@GetMapping(value = "/movies/all")
	public ResponseEntity<?> getAllMovies(@RequestHeader(value = "loggedInUser") String loggedInUser) {
		try {
			return new ResponseEntity<>(movieService.getAllMovies(loggedInUser), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Method to get all tweets of a user HTTP GET Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram
	 */
	@GetMapping(value = "/movies/search/{moviename}")
	public ResponseEntity<?> getMovies(@PathVariable("moviename") String moviename,
			@RequestHeader(value = "loggedInUser") String loggedInUser) {
		try {
			return new ResponseEntity<>(movieService.getMovies(moviename, loggedInUser), HttpStatus.OK);
//		} catch (MovieNotFoundException e) {
//			return new ResponseEntity<Object>(new ErrorResponse("Invalid User param received"),
//					HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (Exception e) {
			return new ResponseEntity<Object>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Controller method to post a new movie HTTP POST Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/movies/ram/add
	 */
	@PostMapping(value = "/movies/{username}/add")
	public ResponseEntity<?> postNewTweet(@PathVariable("username") String username, @RequestBody Movie newMovie) {
//		log.info("posting tweet message sent to: " + KAFKA_TOPIC);
		return new ResponseEntity<>(movieService.postNewMovie(username, newMovie), HttpStatus.CREATED);
	}

	/**
	 * Controller method to get movie and its details HTTP GET Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/movies/ram/ibe2226137b37328
	 */
	@GetMapping(value = "/movies/{moviename}/{movieId}")
	public ResponseEntity<?> getMovieDeatils(@PathVariable("moviename") String moviename,
			@PathVariable("movieId") String movieId) {
		try {
//			kafkaTemplate.send(KAFKA_TOPIC, username + " is fetching a tweet and it's details.");
			return new ResponseEntity<>(movieService.getMovie(movieId, moviename), HttpStatus.OK);
		} catch (Exception e) {
//			kafkaTemplate.send(KAFKA_TOPIC,
//					username + " is fetching a tweet and its details but encountered server error.");
			return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	/**
	 * Controller method to update an existing movie HTTP PUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/update
	 */
//	@PutMapping(value = "/movies/{moviename}/update/{ticket}")
//	public ResponseEntity<?> updateTicket(@PathVariable("username") String username,
//			@RequestBody TicketUpdate ticketUpdate) {
//		try {
////			kafkaTemplate.send(KAFKA_TOPIC, username + " has updated a tweet.");
//			return new ResponseEntity<>(
//					moviService.updateTicket(username, TicketUpdate.getTweetId(), tweetUpdate.getTweetText()),
//					HttpStatus.OK);
//		} catch (TweetNotFoundException e) {
////			kafkaTemplate.send(KAFKA_TOPIC, username + " has encountered an error while updating a tweet.");
//			return new ResponseEntity<>(new ErrorResponse("Given tweetId cannot be found"), HttpStatus.NOT_FOUND);
//		} catch (Exception e) {
////			kafkaTemplate.send(KAFKA_TOPIC, username + " has encountered server error while updating a tweet.");
//			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	/**
	 * Controller method to delete an existing movie HTTP DELETE Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/delete
	 */
	@DeleteMapping(value = "/movies/{moviename}/delete/{movieId}")
	public ResponseEntity<?> deleteMovie(@PathVariable("username") String username,
			@RequestHeader(value = "movieId") String movieId) {
		try {
//			kafkaTemplate.send(KAFKA_TOPIC, username + " has deleted a movie.");
			return new ResponseEntity<>(movieService.deleteMovie(movieId), HttpStatus.OK);
		} catch (MovieNotFoundException e) {
//			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while deleting a tweet");
			return new ResponseEntity<>(new ErrorResponse("Given movieId cannot be found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
//			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a  server error while deleting a tweet");
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * Controller method to like an existing tweet HTTP PUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/like/ibe2226137b37328
	 */
//	@PutMapping(value = "/movies/{username}/like/{movieId}")
//	public ResponseEntity<?> likeAMovie(@PathVariable("username") String username,
////	@PathVariable(value = "movieId") String movieId) {
//			try {
////	kafkaTemplate.send(KAFKA_TOPIC, username + " liked a movie.");
//				return new ResponseEntity<>(movieService.likeMovie(username, movieId), HttpStatus.OK);
//			} catch (MovieNotFoundException e) {
////	kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while liking a movie");
//				return new ResponseEntity<>(new ErrorResponse("Given movieId cannot be found"), HttpStatus.NOT_FOUND);
//			} catch (Exception e) {
////	kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a server error while liking a movie");
//				return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
//			HttpStatus.INTERNAL_SERVER_ERROR);
//}
//}

	/**
	 * Controller method to Dislike an existing tweet HTTP GUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/dislike/ibe2226137b37328
	 */
	@PutMapping(value = "/movies/{username}/dislike/{movieId}")
public ResponseEntity<?> dislikeAMovie(@PathVariable("username") String username,
		@PathVariable(value = "movieId") String movieId) {
	try {
//		kafkaTemplate.send(KAFKA_TOPIC, username + " disliked a movie");
		return new ResponseEntity<>(movieService.dislikeMovie(username, movieId), HttpStatus.OK);
	} catch (MovieNotFoundException e) {
//		kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while disliking a movie");
		return new ResponseEntity<>(new ErrorResponse("Given movieId cannot be found"), HttpStatus.NOT_FOUND);
	} catch (Exception e) {
//		kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a server error while deleting a movie");
		return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

	/**
	 * Controller method to comment on an existing movie HTTP GUT Mapping
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/reply/ibe2226137b37328
	 */
	@PostMapping(value = "/movies/{username}/reply/{movieId}")
	public ResponseEntity<?> replyToMovie(@PathVariable("username") String username,
			@PathVariable("movieId") String movieId, @RequestBody Reply movieReply) {
		try {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has commented on a movie.");
			return new ResponseEntity<>(movieService.replyMovie(username, movieId, movieReply.getComment()),
					HttpStatus.OK);
		} catch (MovieNotFoundException e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd an error while commenting on a movie");
			return new ResponseEntity<>(new ErrorResponse("Given movieId cannot be found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			kafkaTemplate.send(KAFKA_TOPIC, username + " has encounterd a server error while commenting on a movie");
			return new ResponseEntity<>(new ErrorResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
}
