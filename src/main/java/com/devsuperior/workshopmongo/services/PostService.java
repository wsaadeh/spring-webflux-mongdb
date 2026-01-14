package com.devsuperior.workshopmongo.services;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.workshopmongo.dto.PostDTO;
import com.devsuperior.workshopmongo.entities.Post;
import com.devsuperior.workshopmongo.repositories.PostRepository;
import com.devsuperior.workshopmongo.services.exceptions.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

	@Autowired
	private PostRepository repository;

	public Mono<PostDTO> findById(String id) {
		return repository.findById(id).
				map(existingPost -> new PostDTO(existingPost))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Resource not found.")));
	}

	public Flux<PostDTO> findByTitle(String text) {
		Flux<PostDTO> result = repository.searchTitle(text).map(x -> new PostDTO(x));
		return result;
	}

	public Flux<PostDTO> fullSearch(String text, Instant minDate, Instant maxDate) {
		maxDate = maxDate.plusSeconds(86400); // 24 * 60 * 60
		Flux<PostDTO> result = repository.fullSearch(text, minDate, maxDate).map(x -> new PostDTO(x));
		return result;
	}
}
