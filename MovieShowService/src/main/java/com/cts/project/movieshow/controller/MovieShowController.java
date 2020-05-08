package com.cts.project.movieshow.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.cts.project.movieshow.entity.Movie;
import com.cts.project.movieshow.entity.MovieShow;
import com.cts.project.movieshow.entity.Theater;
import com.cts.project.movieshow.errorHandling.CustomErrorType;
import com.cts.project.movieshow.errorHandling.MovieShowNotFoundException;
import com.cts.project.movieshow.repository.MovieShowRepository;

@RestController
@RequestMapping("/movieshow")
public class MovieShowController 
{
	  @Autowired
	  private MovieShowRepository movieShowRepository;
	 
	  @Autowired
	  private WebClient.Builder webClientBuilder;	 
	  
	  private final Logger logger = LoggerFactory.getLogger(this.getClass());

	  @RequestMapping(value = "/", method = RequestMethod.GET)
	  public ResponseEntity<List<MovieShow>> listAllMovieShow() throws IOException
	  {
		    logger.info("Fetching all Movie Show's details..."); 
	        List<MovieShow> movieshow;
	        try 
	        {
	        	movieshow = movieShowRepository.findAll();
	            if (movieshow.isEmpty()) 
	            {
	               return new ResponseEntity<List<MovieShow>>(HttpStatus.NO_CONTENT);
	               // You many decide to return HttpStatus.NOT_FOUND
	            }
	        }
	        catch(RuntimeException e) 
			{ 
				logger.error("RunTimeException is thrown... ");
				throw new RuntimeException(e);
			}
	        
	        return new ResponseEntity<List<MovieShow>>(movieshow, HttpStatus.OK);
	    }
      
      
       @SuppressWarnings({ "unchecked", "rawtypes" })
	   @RequestMapping(value = "/", method = RequestMethod.POST)
       public ResponseEntity<?> createMovieShow(@RequestBody MovieShow movieShow, UriComponentsBuilder ucBuilder) throws IOException
       {
              logger.info("Creating new Movie Show : {}", movieShow);
              Movie movie;
              Theater theater;
              
              try 
              {
            	  movie = webClientBuilder.build() // API call for movie service
                          .get()
                          .uri("http://movie-service/movie/" + movieShow.getMovieId())
                          .retrieve().bodyToMono(Movie.class)
                          .block();
                  
	        	 theater = webClientBuilder.build() // API call for theater service
	                       .get()
	                       .uri("http://theater-service/theater/" + movieShow.getTheaterId())
	                       .retrieve().bodyToMono(Theater.class)
	                       .block();
                  
               } 
    	          
              catch(RuntimeException e)
    	      {
    	              logger.error("Unable to create Movie Show : {} as given movie or theater does not exist", movieShow);
    	              return new ResponseEntity(new CustomErrorType("Unable to create Movie Show with movie_Id: "+ movieShow.getMovieId()+" and theater Id: " + 
    	                      movieShow.getTheaterId() + ", as the entered movie or theater does not exist."),HttpStatus.NOT_FOUND);
    	      }
    	          	          
    	      movieShowRepository.save(movieShow);
              
    	      logger.info("Movie Show : {} created sucessfully...", movieShow);
              HttpHeaders headers = new HttpHeaders();
	          headers.setLocation(ucBuilder.path("/movieshow/{movieshowId}").buildAndExpand(movieShow.getId()).toUri()); 
	          return new ResponseEntity<String>(headers, HttpStatus.CREATED);
       }
   

	   @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{movieshowId}", method = RequestMethod.PUT)
       public ResponseEntity<MovieShow> updateMovieShow(@PathVariable("movieshowId") Integer movieshowId, @RequestBody MovieShow movieShowRequest) throws IOException
       {
		    logger.info("Updating Movie Show with id {}", movieshowId);
	    	MovieShow movieshow;
	    	try 
	    	{
	    		movieshow = movieShowRepository.findById(movieshowId).orElse(null);
	    		
	    		 if (movieshow == null)
				 {
					 logger.error("Movie Show with id {} not found.", movieshowId);
					 return new ResponseEntity(new CustomErrorType("Unable to find Movie Show with id: " +  movieshowId + " ,does not exist."),HttpStatus.NOT_FOUND);
				 }

	    		 movieshow.setsTime(movieShowRequest.getsTime());
	    		
	    	}
	    	catch(RuntimeException e)
	    	{
	    		logger.error("RunTimeException is thrown... ");
				throw new RuntimeException(e);
	    	}

	    	movieShowRepository.save(movieshow);
	    	logger.info("Movie Show : {} updated sucessfully...", movieshow);
	    	return new ResponseEntity<MovieShow>(movieshow, HttpStatus.OK);
   
       }


        @RequestMapping(value = "/{movieshowId}", method = RequestMethod.DELETE)
	    public ResponseEntity<?> deleteMovieShow(@PathVariable (value="movieshowId") Integer movieShowId) 
        {
        	 logger.info("Deleting Movie Showswith id {}", movieShowId);
        	 
	         return movieShowRepository.findById(movieShowId).map(movieShow -> {
	         	movieShowRepository.delete(movieShow);
	             return ResponseEntity.ok().build();
	         }).orElseThrow(() -> new MovieShowNotFoundException("Movie Show Id : " + movieShowId + " not found"));
	     }
        
	      
	     @RequestMapping(value = "/", method = RequestMethod.DELETE)
	     public ResponseEntity<MovieShow> deleteAllMovieShow() 
	     {
	          logger.info("Deleting All Movie Shows...");
	   
	          movieShowRepository.deleteAll();
	          return new ResponseEntity<MovieShow>(HttpStatus.NO_CONTENT);
	     }

}
