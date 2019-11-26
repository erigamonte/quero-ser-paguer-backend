package br.com.pague.desafio.service.mapper;

import org.mapstruct.Mapper;

import br.com.pague.desafio.domain.Cliente;
import br.com.pague.desafio.service.dto.ClienteDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ClienteMapper extends EntityMapper <ClienteDTO, Cliente> {

    ClienteDTO toDto(Cliente cliente); 

    Cliente toEntity(ClienteDTO clienteDTO); 
    
    default Cliente fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }
}
