package com.cts.project.movieshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.cts.project.movieshow.entity.MovieShow;

@RepositoryRestResource
public interface MovieShowRepository extends JpaRepository<MovieShow, Integer> 
{
	
}
