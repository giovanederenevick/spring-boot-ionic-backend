package br.com.giovanefilho.cursomc2.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import br.com.giovanefilho.cursomc2.domain.enums.Perfil;
import br.com.giovanefilho.cursomc2.security.UserSS;
import br.com.giovanefilho.cursomc2.services.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.giovanefilho.cursomc2.domain.Cidade;
import br.com.giovanefilho.cursomc2.domain.Cliente;
import br.com.giovanefilho.cursomc2.domain.Endereco;
import br.com.giovanefilho.cursomc2.domain.enums.TipoCliente;
import br.com.giovanefilho.cursomc2.dto.ClienteDTO;
import br.com.giovanefilho.cursomc2.dto.ClienteNewDTO;
import br.com.giovanefilho.cursomc2.repositories.ClienteRepository;
import br.com.giovanefilho.cursomc2.repositories.EnderecoRepository;
import br.com.giovanefilho.cursomc2.services.exceptions.DataIntegrityException;
import br.com.giovanefilho.cursomc2.services.exceptions.ObjectNotFoundException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepo;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private S3Service s3Service;

	public Cliente find(Integer id) {

		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}

		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() ->  new ObjectNotFoundException("Objeto não encontrado. ID: " 
				+ id 
				+ ", Tipo: "
				+ Cliente.class.getName()
				));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepo.saveAll(obj.getEnderecos());
		
		return obj;
	}
	
	public Cliente update(Cliente obj) {

		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		
		find(id);
		
		try {
			repo.deleteById(id);			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados.");
		}
	}

	public List<Cliente> findAll() {
		
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipoCliente()), pe.encode(objDto.getSenha()));
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cliente, new Cidade(objDto.getCidadeId(), null, null));
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		
		return cliente;
	}

	private void updateData(Cliente newObj, Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {

		return s3Service.uploadFile(multipartFile);
	}
}
