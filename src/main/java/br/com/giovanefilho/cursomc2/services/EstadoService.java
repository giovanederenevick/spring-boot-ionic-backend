package br.com.giovanefilho.cursomc2.services;

import br.com.giovanefilho.cursomc2.domain.Cliente;
import br.com.giovanefilho.cursomc2.domain.Estado;
import br.com.giovanefilho.cursomc2.repositories.EstadoRepository;
import br.com.giovanefilho.cursomc2.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> findAll() {

        return estadoRepository.findAllByOrderByNome();
    }
}
