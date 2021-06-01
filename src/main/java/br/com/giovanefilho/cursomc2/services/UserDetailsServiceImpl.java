package br.com.giovanefilho.cursomc2.services;

import br.com.giovanefilho.cursomc2.domain.Cliente;
import br.com.giovanefilho.cursomc2.repositories.ClienteRepository;
import br.com.giovanefilho.cursomc2.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Cliente> obj = repo.findByEmail(email);
        Cliente cli = obj.orElseThrow(() -> new UsernameNotFoundException(email));

        return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
    }
}
