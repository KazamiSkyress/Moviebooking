package com.example.movie.exception;

public class MovieNotFoundException extends Exception{


		/**
		 *  This exception is thrown when movie is not found
		 */
		private static final long serialVersionUID = 1L;

		public MovieNotFoundException(String msg) {
			super(msg);
		}

}
