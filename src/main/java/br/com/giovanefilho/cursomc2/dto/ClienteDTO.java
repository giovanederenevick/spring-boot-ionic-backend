package br.com.giovanefilho.cursomc2.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import br.com.giovanefilho.cursomc2.domain.Cliente;
import br.com.giovanefilho.cursomc2.services.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotBlank(message = "Preenchimento obrigatório")
	@Length(min = 5, max = 120, message = "O tamanho deve ser entre 5 e 120 caracteres")
	private String nome;
	
	@NotBlank(message = "Preenchimento obrigatório")
	@Email(message = "Email inválido")
	private String email;
	
	public ClienteDTO() {
	}
	
	public ClienteDTO(Cliente obj) {
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
