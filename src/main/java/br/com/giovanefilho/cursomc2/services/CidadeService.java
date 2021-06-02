package br.com.giovanefilho.cursomc2.services;

import br.com.giovanefilho.cursomc2.domain.Cidade;
import br.com.giovanefilho.cursomc2.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public List<Cidade> findByEstado(Integer estadoId) {

        return cidadeRepository.findCidades(estadoId);
    }
}
