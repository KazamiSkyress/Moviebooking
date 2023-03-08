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
public class NewPassword {
	private String newPassword;
	private String contact;
	public String getNewPassword() {
		// TODO Auto-generated method stub
		return newPassword;
	}
	public String getContact() {
		// TODO Auto-generated method stub
		return contact;
	}

}
