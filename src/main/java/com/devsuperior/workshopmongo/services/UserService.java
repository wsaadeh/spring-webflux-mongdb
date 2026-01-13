package com.devsuperior.workshopmongo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.workshopmongo.dto.PostDTO;
import com.devsuperior.workshopmongo.dto.UserDTO;
import com.devsuperior.workshopmongo.entities.User;
import com.devsuperior.workshopmongo.repositories.UserRepository;
import com.devsuperior.workshopmongo.services.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;


	public Flux<UserDTO> findAll() {
		Flux<UserDTO> result = repository.findAll().map(x -> new UserDTO(x));
		return result;
	}

	public Mono<UserDTO> findById(String id){
		return repository.findById(id).map(x->new UserDTO(x))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Resource not found.")));
	}

	public Mono<UserDTO> insert(UserDTO dto){
		User entity = new User();
		copyDTOtoUser(dto,entity);
		Mono<UserDTO> result = repository.save(entity).map(user->new UserDTO(user));
		return result;
	}

	private void copyDTOtoUser(UserDTO dto, User entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
	}

}
