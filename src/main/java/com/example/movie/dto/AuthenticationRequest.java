package com.example.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Bhakti Joshi
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

	private String username;

	private String password;

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}


}
