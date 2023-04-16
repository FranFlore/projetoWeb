package com.projetoweb.application.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.Long;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.projetoweb.application.model.user.User;
import com.projetoweb.application.repository.user.UserRepository;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository  userRepository ;	
	
	@PostMapping("/create")
	public ResponseEntity<User> create(@RequestBody User user) {
		try {
			Long userId = user.getId();
			if(userId.compareTo(0L) != 0) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);	
			}
			userRepository.save(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<User> update(@RequestBody User user) {
		try {
			Long userId = user.getId();
			if(userId.compareTo(0L) == 0) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);	
			}
			userRepository.save(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PatchMapping("/partialUpdate/{id}")
	public ResponseEntity<User> partialUpdate(@RequestBody Map<String, Object> partialUser, @PathVariable("id") String id) {
		try {
			if(StringUtils.isEmpty(id)) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);	
			}
			Optional<User> optionalUser = userRepository.findById(Long.parseLong(id));
			if(!optionalUser.isPresent()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			User storedUser = optionalUser.get();
			for (Map.Entry<String, Object> entry : partialUser.entrySet()) {
				if("nome".equalsIgnoreCase(entry.getKey())){
					storedUser.setNome(String.valueOf(entry.getValue()));
				}
				if("cpf".equalsIgnoreCase(entry.getKey())){
					storedUser.setCpf(String.valueOf(entry.getValue()));
				}
				if("email".equalsIgnoreCase(entry.getKey())){
					storedUser.setEmail(String.valueOf(entry.getValue()));
				}
				if("celular".equalsIgnoreCase(entry.getKey())){
					storedUser.setCelular(String.valueOf(entry.getValue()));
				}
				if("dataNascimento".equalsIgnoreCase(entry.getKey())){
					storedUser.setDataNascimento(Date.valueOf(String.valueOf(entry.getValue())));
				}
				if("sexo".equalsIgnoreCase(entry.getKey())){
					storedUser.setSexo(String.valueOf(entry.getValue()));
				}
			}
			User savedUser = userRepository.save(storedUser);
			return new ResponseEntity<>(savedUser, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	

	@DeleteMapping("/delete")
	public ResponseEntity<User> delete(@RequestBody User user) {
		try {
			Long userId = user.getId();
			if(userId.compareTo(0L) == 0) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);	
			}
			userRepository.delete(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/lista")
	public ResponseEntity<List<User>> findAll() {
		try {
			List<User> returnList = userRepository.findAll();
			if(returnList.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
			return new ResponseEntity<>(returnList, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/obtem/{id}")
	public ResponseEntity<User> getById(@PathVariable String id) {
		try {
			Optional<User> returnUser = userRepository.findById(Long.valueOf(id));
			if(!returnUser.isPresent())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
			return new ResponseEntity<>(returnUser.get(), HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/filtro")
	public ResponseEntity<List<User>> getByFilter(@RequestBody Map<String, Object> genericFilter) {
		try {
			List<User> returnList = userRepository.findAll();
			if(returnList.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
			for (Map.Entry<String, Object> entry : genericFilter.entrySet()) {
				if("nome".equalsIgnoreCase(entry.getKey())){
					returnList = returnList.stream().filter(user -> user.getNome().equalsIgnoreCase(String.valueOf(entry.getValue()))).toList();
				}
				if("cpf".equalsIgnoreCase(entry.getKey())){
					returnList = returnList.stream().filter(user -> user.getCpf().equalsIgnoreCase(String.valueOf(entry.getValue()))).toList();
				}
				if("email".equalsIgnoreCase(entry.getKey())){
					returnList = returnList.stream().filter(user -> user.getEmail().equalsIgnoreCase(String.valueOf(entry.getValue()))).toList();
				}
				if("celular".equalsIgnoreCase(entry.getKey())){
					returnList = returnList.stream().filter(user -> user.getCelular().equalsIgnoreCase(String.valueOf(entry.getValue()))).toList();
				}
				if("dataNascimento".equalsIgnoreCase(entry.getKey())){
					returnList = returnList.stream().filter(user -> user.getDataNascimento().compareTo(Date.valueOf(String.valueOf(entry.getValue()))) == 0).toList();
				}
				if("sexo".equalsIgnoreCase(entry.getKey())){
					returnList = returnList.stream().filter(user -> user.getSexo().equalsIgnoreCase(String.valueOf(entry.getValue()))).toList();
				}
			}
			if(returnList.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			return new ResponseEntity<>(returnList, HttpStatus.OK);
		} catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
