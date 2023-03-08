package com.example.movie.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;


import com.example.movie.dto.Comment;
import com.example.movie.dto.MovieResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Movie {
	
	private static final long serialVersionUID = 1L;
	@Id
	private String movieId;

	private String moviename;

	private String theatreName;

	private String NumberofTickets;

	private String releaseDate;
	
	private List<String> likes = new ArrayList<>();

	private List<Comment> comments = new ArrayList<>();

//	public List<MovieResponse> getLikes() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public List<MovieResponse> getComments() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Object getMovieId() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Object getFirstName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Object getMovieText() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Object getLastName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Object getReleaseDate() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void setMovie(String updatedMovie) {
//		// TODO Auto-generated method stub
//		
//	}
//	


}
