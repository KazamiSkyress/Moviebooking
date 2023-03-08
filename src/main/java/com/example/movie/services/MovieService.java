package com.example.movie.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.movie.dto.Comment;
import com.example.movie.dto.MovieResponse;
import com.example.movie.entity.Movie;
import com.example.movie.exception.MovieNotFoundException;
import com.example.movie.dto.MovieResponse;
import com.example.movie.entity.Movie;
import com.example.movie.exception.InvalidUsernameException;
import com.example.movie.exception.MovieNotFoundException;
import com.example.movie.repository.MovieRepository;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * @author Bhakti Joshi
 */
@Service
public class MovieService {

//	Injected TweetRepository bean
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	KafkaTemplate<String, Movie> kafkaTemplate;
	
	private static final String KAFKA_TOPIC = "movies";
	
	Logger logger = LoggerFactory.getLogger(MovieService.class);
	/**
	 * Find all the available movies
	 * 
	 * @return TweetResponse
	 */
	public List<MovieResponse> getAllMovies(String loggedInUser) {
		List<Movie> Movie1 = movieRepository.findAll();
		List<MovieResponse> movieResponse = Movie1.stream().map(movie -> {
			Integer likesCount = movie.getLikes().size();
			Boolean likeStatus = movie.getLikes().contains(loggedInUser);
			Integer commentsCount = movie.getComments().size();
			logger.info("All tweets --> {}",movie);
			return new MovieResponse(movie.getMovieId(), movie.getMoviename(),
					 movie.getReleaseDate(), likesCount, commentsCount,
					likeStatus, movie.getComments());
		}).collect(Collectors.toList());
		return movieResponse;
	}

	/**
	 * Method for searching movies by a particular user
	 * 
	 * @return MovieResponse
	 * @throws InvalidUsernameException
	 */
	public List<MovieResponse> getUserMovies(String moviename, String loggedInUser) throws InvalidUsernameException {
		// use username as login id
		if (!StringUtils.isBlank(moviename)) {
			List<Movie> tweets = movieRepository.findByMoviename(moviename);
			// List<TweetResponse> tweetResponse= new ArrayList<>();
			List<MovieResponse> movieResponse = tweets.stream().map(movie -> {
				Integer likesCount = movie.getLikes().size();
				Boolean likeStatus = movie.getLikes().contains(loggedInUser);
				Integer commentsCount = movie.getComments().size();
				logger.info( moviename+" movies --> {}",movie);
				return new MovieResponse(movie.getMovieId(), moviename,movie.getReleaseDate(), likesCount, commentsCount, likeStatus,
						movie.getComments());
			}).collect(Collectors.toList());
			return movieResponse;
		} else {
			throw new InvalidUsernameException("Username/loginId provided is invalid");
		}

	}

	/**
	 * Method to post a new Movie
	 * 
	 * @return Movie
	 */
	public Movie postNewMovie(String username, Movie newMovie) {
		//newTweet.setTweetId(UUID.randomUUID().toString());
		kafkaTemplate.send(KAFKA_TOPIC, newMovie);
		logger.info("The new tweet --> {}",newMovie);
		return movieRepository.insert(newMovie);
	}

	/**
	 * Method to get movie with movieid
	 * 
	 * @return MovieResponse
	 * @throws MovieNotFoundException
	 */
	public MovieResponse getMovie(String movieId, String moviename) throws MovieNotFoundException {
		Optional<Movie> movieFounded = movieRepository.findById(movieId);
		if (movieFounded.isPresent()) {
			Movie movie = movieFounded.get();
			Integer likesCount = movie.getLikes().size();
			Boolean likeStatus = movie.getLikes().contains(moviename);
			Integer commentsCount = movie.getComments().size();
			logger.info("returned movie --> {}",movie);
			return new MovieResponse(movie.getMovieId(), movie.getMoviename(), movie.getReleaseDate(), likesCount, commentsCount,
					likeStatus, movie.getComments());
		} else {
			throw new MovieNotFoundException("This mvie does not exist anymore.");
		}

	}
	/**
	 * Method to update an existing movie
	 * 
	 * @return Movie
	 * @throws MovieNotFoundException
	 */
//	public Movie updateMovie(String userId, String movieId, String updatedMovie) throws MovieNotFoundException {
//
//		Optional<Movie> originalMovieOptional = movieRepository.findById(movieId);
//		if (originalMovieOptional.isPresent()) {
//			Movie movie = originalMovieOptional.get();
//			movie.setMovie(updatedMovie);
//			logger.info("Updated movie --> {}",movie);
//			return movieRepository.save(movie);
//		} else {
//			logger.error("cannot update movie since this movie does not exist anymore.");
//			throw new MovieNotFoundException("This movie does not exist anymore.");
//		}
//
//	}

	/**
	 * Method to delete an existing movie
	 * 
	 * @return boolean
	 * @throws MovieNotFoundException
	 */
	public boolean deleteMovie(String movieId) throws MovieNotFoundException {
		if (movieRepository.existsById(movieId) && !StringUtils.isBlank(movieId)) {
			movieRepository.deleteById(movieId);
			return true;
		} else {
			logger.error("Cannot delete movie since this movie does not exist anymore.");
			throw new MovieNotFoundException("This movie does not exist anymore.");
		}
	}

	/**
	 * Method to like an existing movie
	 * 
	 * @retun movie
	 * @throws MovieNotFoundException
	 */
	public Movie likeMovie(String moviename, String movieId) throws MovieNotFoundException {
		Optional<Movie> movieOptional = movieRepository.findById(movieId);
		if (movieOptional.isPresent()) {
			Movie movie = movieOptional.get();
			movie.getLikes().add(moviename);
			logger.debug("Liked movie --> {}",movie);
			return movieRepository.save(movie);
		} else {
			logger.error("cannot like movie since this movie does not exist anymore.");
			throw new MovieNotFoundException("This movie does not exist anymore.");
		}
	}
	/**
	 * Method to DisLike an existing movie
	 * 
	 * @retun movie
	 * @throws MovieNotFoundException
	 */
	public Movie dislikeMovie(String moviename, String movieId) throws MovieNotFoundException {
		Optional<Movie> movieOptional = movieRepository.findById(movieId);
		if (movieOptional.isPresent()) {
			Movie movie = movieOptional.get();
			movie.getLikes().remove(moviename);
			logger.info("Disliked movie --> {}",movie);
			return movieRepository.save(movie);
		} else {
			logger.error("cannot dislike movie since this movie does not exist anymore.");
			throw new MovieNotFoundException("This movie does not exist anymore.");
		}
	}

	/**
	 * Method to comment on a movie
	 * 
	 * @return movie
	 * @throws MovieNotFoundException
	 */
	public Movie replyMovie(String moviename, String movieId, String movieReply) throws MovieNotFoundException {
		Optional<Movie> movieFounded = movieRepository.findById(movieId);
		if (movieFounded.isPresent()) {
			Movie movie = movieFounded.get();
			Comment comment = new Comment(moviename, movieReply);
			List<Comment> addList = new ArrayList<Comment>();
			addList.add(comment);
			movie.getComments().add(comment);
			kafkaTemplate.send(KAFKA_TOPIC, movie); // KAFKA SENDING EVENTS TO TOPIC
			logger.info("Commented on movie --> {}",movie);
			return movieRepository.save(movie);
		} else {
			logger.error("cannot comment on movie since this movie does not exist anymore.");
			throw new MovieNotFoundException("This movie does not exist anymore.");
		}
	}

	public Object getMovies(String moviename, String loggedInUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
