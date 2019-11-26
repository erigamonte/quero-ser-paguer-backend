package br.com.pague.desafio.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.pague.desafio.domain.ItemPedido;
import br.com.pague.desafio.service.dto.ItemPedidoDTO;

@Mapper(componentModel = "spring", uses = {PedidoMapper.class, ProdutoMapper.class})
public interface ItemPedidoMapper extends EntityMapper <ItemPedidoDTO, ItemPedido> {

	@Mapping(source = "pedido.id", target = "pedidoId")
	@Mapping(source = "produto.id", target = "produtoId")
    ItemPedidoDTO toDto(ItemPedido itemPedido); 

	@Mapping(source = "pedidoId", target = "pedido")
	@Mapping(source = "produtoId", target = "produto")
    ItemPedido toEntity(ItemPedidoDTO itemPedidoDTO); 
    
    default ItemPedido fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(id);
        return itemPedido;
    }
}
