package br.com.pague.desafio.service.mapper;

import org.mapstruct.Mapper;

import br.com.pague.desafio.domain.Produto;
import br.com.pague.desafio.service.dto.ProdutoDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ProdutoMapper extends EntityMapper <ProdutoDTO, Produto> {

	ProdutoDTO toDto(Produto produto); 

	Produto toEntity(ProdutoDTO produtoDTO); 
    
    default Produto fromId(Long id) {
        if (id == null) {
            return null;
        }
        Produto produto = new Produto();
        produto.setId(id);
        return produto;
    }
}
