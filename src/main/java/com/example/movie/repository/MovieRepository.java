package com.example.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.movie.entity.Movie;


import java.lang.String;
import java.util.List;



public interface MovieRepository extends MongoRepository <Movie , String> {

	List<Movie> findByMoviename(String moviename);

	/**
	 * @return list of movies available with moviesID
	 * */
	List<Movie> findByMovieId(String movieId);
}
