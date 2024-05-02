package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ClienteDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Cliente;

import java.util.List;

public interface IClienteService {

    public List<ClienteDTO> getClientes();

    public Cliente saveCliente(Cliente cliente);

    public void deleteCliente(Long id);

    public Cliente findCliente(Long id);

    public void updateCliente(long id, ClienteDTO clienteDTO);
}
