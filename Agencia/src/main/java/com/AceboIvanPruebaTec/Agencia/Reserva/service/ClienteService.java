package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ClienteDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.*;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    /**
     * Obtiene una lista de clientes del repositorio que no han sido marcados como borrados.
     * Si no se encuentran clientes, se lanza una excepción NoSuchElementException.
     * Utiliza la API de Streams para mapear cada cliente a su correspondiente DTO de cliente
     * utilizando el método getClienteDTO(). Finalmente, devuelve una lista de DTO de clientes.
     *
     * @return Lista de DTO de clientes.
     * @throws NoSuchElementException Si no se encuentran clientes.
     */
    @Override
    public List<ClienteDTO> getClientes() {
        List<Cliente> clientes = clienteRepository.findNotDeletedClientes();

        if (clientes.isEmpty()) {
            throw new NoSuchElementException("No se encontraron clientes.");
        }

        return clientes.stream()
                .map(cliente -> getClienteDTO(cliente))
                .collect(Collectors.toList());
    }


    /**
     * Guarda un cliente en el repositorio si el DNI no existe previamente.
     *
     * @param cliente El Cliente que se va a guardar.
     * @return El cliente guardado si se realiza con éxito, de lo contrario null.
     * @throws IllegalArgumentException Si el DNI del cliente ya existe en la base de datos.
     */
    @Override
    public Cliente saveCliente(Cliente cliente) {
        Cliente existingCliente = clienteRepository.findByDni(cliente.getDni());
        if (existingCliente != null) {
            throw new IllegalArgumentException("El cliente con DNI " + cliente.getDni() + " ya existe en la base de datos.");
        }

        // Actualizar asociaciones con las reservas de habitaciones
        cliente.getReservasHabitacion().forEach(reservaHabitacion -> reservaHabitacion.setCliente(cliente));

        // Actualizar asociaciones con las reservas de vuelos
        cliente.getReservasVuelo().forEach(reservaVuelo -> reservaVuelo.setCliente(cliente));

        return clienteRepository.save(cliente);
    }

    /**
     * Elimina lógicamente un cliente de la base de datos.
     * Antes de eliminar al cliente, verifica si tiene reservas de habitaciones o vuelos activas.
     *
     * @param id El ID del cliente que se va a eliminar.
     * @throws IllegalStateException  Si el cliente tiene reservas de habitaciones o vuelos activas.
     * @throws IllegalStateException  Si el cliente ya ha sido marcado como borrado previamente.
     * @throws NoSuchElementException Si no se encuentra ningún cliente con el ID proporcionado.
     */
    @Override
    public void deleteCliente(Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);

        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();

            boolean hasActiveReservations = cliente.getReservasHabitacion().stream().anyMatch(reserva -> !reserva.getBorrado())
                    || cliente.getReservasVuelo().stream().anyMatch(reserva -> !reserva.getBorrado());

            if (hasActiveReservations) {
                throw new IllegalStateException("No se puede eliminar el cliente con ID " + id + " porque tiene reservas activas.");
            } else {
                if (!cliente.getBorrado()) {
                    cliente.setBorrado(true);
                    clienteRepository.save(cliente);
                } else {
                    throw new IllegalStateException("El cliente con ID " + id + " ya ha sido marcado como borrado.");
                }
            }
        } else {
            throw new NoSuchElementException("No se encontró ningún cliente con el ID proporcionado: " + id);
        }
    }


    /**
     * Busca un cliente en el repositorio por su ID, asegurándose de que no esté marcado como borrado.
     * Si se encuentra el cliente con el ID proporcionado y no está marcado como borrado, lo devuelve.
     * De lo contrario, lanza una excepción NoSuchElementException.
     *
     * @param id El ID del cliente a buscar.
     * @return El cliente encontrado.
     * @throws NoSuchElementException Si no se encuentra ningún cliente con el ID proporcionado
     *                                o si el cliente encontrado está marcado como borrado.
     */
    @Override
    public Cliente findCliente(Long id) {
        Cliente cliente = clienteRepository.findByIdAndBorradoFalse(id);

        if (cliente == null) {
            throw new NoSuchElementException("No se encontró ningún cliente con el ID " + id);
        }

        return cliente;
    }

    /**
     * Actualiza un cliente en el repositorio.
     * Además de actualizar los datos del cliente, este método ajusta las asociaciones con las reservas de habitaciones y vuelos si es necesario.
     *
     * @param id         El ID del cliente que se va a actualizar.
     * @param clienteDTO El DTO del cliente con los datos actualizados.
     * @throws NoSuchElementException Si no se encuentra ningún cliente con el ID proporcionado.
     */
    @Override
    public void updateCliente(long id, ClienteDTO clienteDTO) {
        // Busca el cliente por su ID
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);

        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();

            // Actualiza los datos del cliente con los proporcionados en el DTO
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setApellido(clienteDTO.getApellido());
            cliente.setDni(clienteDTO.getDni());
            cliente.setEmail(clienteDTO.getEmail());
            cliente.setTelefono(clienteDTO.getTelefono());

            // Ajusta la lógica para actualizar las reservas de habitaciones si es necesario
            List<ReservaHabitacion> nuevasReservasHabitacion = clienteDTO.getReservasHabitacion();
            if (nuevasReservasHabitacion != null) {
                cliente.getReservasHabitacion().clear(); // Borra las reservas existentes del cliente
                cliente.getReservasHabitacion().addAll(nuevasReservasHabitacion); // Agrega las nuevas reservas
            }

            // Ajusta la lógica para actualizar las reservas de vuelos si es necesario
            List<ReservaVuelo> nuevasReservasVuelo = clienteDTO.getReservasVuelo();
            if (nuevasReservasVuelo != null) {
                cliente.getReservasVuelo().clear(); // Borra las reservas existentes del cliente
                cliente.getReservasVuelo().addAll(nuevasReservasVuelo); // Agrega las nuevas reservas
            }

            // Guarda el cliente actualizado en el repositorio
            clienteRepository.save(cliente);
        } else {
            throw new NoSuchElementException("No se encontró ningún cliente con el ID proporcionado: " + id);
        }
    }

    /**
     * Convierte un objeto Cliente en un DTO (Data Transfer Object) de Cliente.
     *
     * @param cliente El objeto Cliente a convertir.
     * @return El DTO de Cliente generado.
     */
    public ClienteDTO getClienteDTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();

        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setApellido(cliente.getApellido());
        clienteDTO.setDni(cliente.getDni());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setTelefono(cliente.getTelefono());
        clienteDTO.setReservasHabitacion(cliente.getReservasHabitacion());
        clienteDTO.setReservasVuelo(cliente.getReservasVuelo());

        return clienteDTO;
    }

}
