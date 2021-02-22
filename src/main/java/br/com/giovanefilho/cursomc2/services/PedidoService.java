package br.com.giovanefilho.cursomc2.services;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.giovanefilho.cursomc2.domain.ItemPedido;
import br.com.giovanefilho.cursomc2.domain.PagamentoComBoleto;
import br.com.giovanefilho.cursomc2.domain.Pedido;
import br.com.giovanefilho.cursomc2.domain.enums.EstadoPagamento;
import br.com.giovanefilho.cursomc2.repositories.ItemPedidoRepository;
import br.com.giovanefilho.cursomc2.repositories.PagamentoRepository;
import br.com.giovanefilho.cursomc2.repositories.PedidoRepository;
import br.com.giovanefilho.cursomc2.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;

	public Pedido find(Integer id) {
		
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() ->  new ObjectNotFoundException("Objeto não encontrado. ID: " 
				+ id 
				+ ", Tipo: "
				+ Pedido.class.getName()
				));
	}

	@Transactional
	public Pedido insert(@Valid Pedido obj) {
		
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		System.out.println(obj);
		
		return obj;	
	} 
}
