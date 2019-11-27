package br.com.pague.desafio.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.domain.Produto;
import br.com.pague.desafio.repository.ItemPedidoRepository;
import br.com.pague.desafio.repository.ProdutoRepository;
import br.com.pague.desafio.service.ProdutoService;
import br.com.pague.desafio.service.dto.ProdutoDTO;
import br.com.pague.desafio.service.mapper.ProdutoMapper;

@Service
public class ProdutoServiceImpl implements ProdutoService{
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoMapper produtoMapper;

	@Override
	@Transactional
	public ProdutoDTO salva(ProdutoDTO dto) throws DesafioException {
		Produto produto = produtoMapper.toEntity(dto);
		produto = produtoRepository.save(produto);
		return produtoMapper.toDto(produto);
	}

	@Override
	@Transactional
	public ProdutoDTO atualiza(ProdutoDTO dto) throws DesafioException {
		Optional<Produto> optional = produtoRepository.findById(dto.getId());
		if (optional.isPresent()) {
			Produto produto = produtoRepository.save(produtoMapper.toEntity(dto));
			return produtoMapper.toDto(produto);
		}
		
		throw new DesafioException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado");
	}
	
	@Override
	public List<ProdutoDTO> obtemTodos() {
		return obtemTodos(null);
	}

	@Override
	public List<ProdutoDTO> obtemTodos(String nome) {
		List<Produto> produtos = null;
		if (nome == null) {
			produtos = produtoRepository.findAll();
		} else {
			produtos = produtoRepository.findAllByNome(nome);
		}
		return produtoMapper.toDto(produtos);
	}

	@Override
	public ProdutoDTO obtemPeloId(Long id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			return produtoMapper.toDto(optional.get());
		}
		
		return null;
	}

	@Override
	@Transactional
	public void remove(Long id) throws DesafioException {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			
			boolean produtoPossuiPedido = itemPedidoRepository.existsByProdutoId(id);
			
			if(produtoPossuiPedido) {
				throw new DesafioException("PRODUTO_POSSUI_PEDIDO", "Produto não pode ser removido, pois possui pedidos efetuados");
			}

			produtoRepository.deleteById(id);
		}
		
		throw new DesafioException("PRODUTO_NAO_ENCONTRADO", "Produto não encontrado");
	}

}
