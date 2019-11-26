package br.com.pague.desafio.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.pague.desafio.domain.Pedido;
import br.com.pague.desafio.service.dto.PedidoDTO;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class})
public interface PedidoMapper extends EntityMapper <PedidoDTO, Pedido> {

	@Mapping(source = "cliente.id", target = "clienteId")
    PedidoDTO toDto(Pedido pedido); 

	@Mapping(source = "clienteId", target = "cliente.id")
    Pedido toEntity(PedidoDTO pedidoDTO); 
    
    default Pedido fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pedido pedido = new Pedido();
        pedido.setId(id);
        return pedido;
    }
}
