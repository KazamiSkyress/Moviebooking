package com.example.movie.dto;





import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import com.example.movie.entity.UserModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MovieResponse implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String movieId;
	private String moviename;
	private String ReleaseDate;
	private Integer likesCount;
	private Integer commentsCount;
	private Boolean likeStatus;
	private List<Comment> comments = new ArrayList<>();

}


