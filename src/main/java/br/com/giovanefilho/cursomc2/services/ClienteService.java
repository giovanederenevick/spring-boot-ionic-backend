package br.com.giovanefilho.cursomc2.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.giovanefilho.cursomc2.domain.Cliente;
import br.com.giovanefilho.cursomc2.repositories.ClienteRepository;
import br.com.giovanefilho.cursomc2.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;

	public Cliente buscar(Integer id) {
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() ->  new ObjectNotFoundException("Objeto não encontrado. ID: " 
				+ id 
				+ ", Tipo: "
				+ Cliente.class.getName()
				));
	}
}
