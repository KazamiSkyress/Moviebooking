package com.example.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.movie.entity.UserModel;

@Repository
public interface UserRepository extends MongoRepository <UserModel,String> {
	
	/**
	 * Persistance Layer for UserModel
	 * @author Bhakti Joshi
	 */
	

		/**
		 * @return userModel for respective username
		 * */
		UserModel findByUsername(String username);

}
