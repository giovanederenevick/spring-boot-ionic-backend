package br.com.giovanefilho.cursomc2.services.validation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.giovanefilho.cursomc2.domain.Cliente;
import br.com.giovanefilho.cursomc2.domain.enums.TipoCliente;
import br.com.giovanefilho.cursomc2.dto.ClienteNewDTO;
import br.com.giovanefilho.cursomc2.repositories.ClienteRepository;
import br.com.giovanefilho.cursomc2.resources.exceptions.FieldMessage;
import br.com.giovanefilho.cursomc2.services.validation.utils.BR;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}
	
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipoCliente()==TipoCliente.PESSOAFISICA.getCod() && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if (objDto.getTipoCliente()==TipoCliente.PESSOAJURIDICA.getCod() && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}

		Optional<Cliente> obj = repo.findByEmail(objDto.getEmail());
		Cliente aux = obj.orElseThrow(() -> new UsernameNotFoundException(objDto.getEmail()));

		if (aux!=null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage())
			.addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
}