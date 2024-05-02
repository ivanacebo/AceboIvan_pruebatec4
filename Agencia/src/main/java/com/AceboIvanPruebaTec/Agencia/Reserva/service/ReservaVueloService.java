package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ReservaVueloDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Cliente;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaVuelo;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IReservaVueloRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaVueloService implements IReservaVueloService {

    @Autowired
    private IReservaVueloRepository reservaVueloRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VueloService vueloService;

    /**
     * Obtiene una lista de todas las reservas de vuelos activas.
     *
     * @return Lista de reservas de vuelos activas.
     * @throws NoSuchElementException Si no se encuentran reservas de vuelos activas.
     */
    @Override
    public List<ReservaVueloDTO> getReservaVuelos() {
        List<ReservaVuelo> reservaVuelos = reservaVueloRepository.findNotDeletedReservasvuelo();

        if (reservaVuelos.isEmpty()) {
            throw new NoSuchElementException("No se encontraron reservas para vuelos.");
        }

        return reservaVuelos.stream()
                .map(reservaVuelo -> getReservaVuelo(reservaVuelo))
                .collect(Collectors.toList());
    }

    /**
     * Elimina lógicamente una reserva de vuelo mediante su identificador.
     * Si la reserva de vuelo se encuentra y no ha sido marcada como borrada previamente,
     * se marca como borrada y se guarda en el repositorio. Si la reserva de vuelo ya ha sido
     * marcada como borrada, se lanza una excepción.
     *
     * @param id El identificador de la reserva de vuelo a eliminar.
     * @return La reserva de vuelo marcada como borrada si se realiza con éxito, de lo contrario null.
     * @throws IllegalStateException Si la reserva de vuelo ya ha sido marcada como borrada anteriormente.
     */
    @Override
    public ReservaVuelo deleteReservaVuelo(Long id) {
        Optional<ReservaVuelo> optionalReservaVuelo = reservaVueloRepository.findById(id);
        if (optionalReservaVuelo.isPresent()) {
            ReservaVuelo reservaVuelo = optionalReservaVuelo.get();
            if (reservaVuelo.getBorrado()) {
                throw new IllegalStateException("La reserva de vuelo con ID " + id + " ya ha sido borrada.");
            } else {
                reservaVuelo.setBorrado(true);
                return reservaVueloRepository.save(reservaVuelo);
            }
        } else {
            return null;
        }
    }


    /**
     * Busca una reserva de vuelo por su ID y asegura que no esté marcada como borrada.
     *
     * @param id El ID de la reserva de vuelo a buscar.
     * @return La reserva de vuelo encontrada si existe y no está marcada como borrada.
     * @throws NoSuchElementException Si no se encuentra ninguna reserva de vuelo con el ID especificado
     *                                o si la reserva encontrada está marcada como borrada.
     */
    @Override
    public ReservaVuelo findReservaVuelo(Long id) {
        ReservaVuelo reservaVuelo = reservaVueloRepository.findByIdAndBorradoFalse(id);

        if (reservaVuelo == null) {
            throw new NoSuchElementException("No se encontró ninguna reserva de vuelo con el ID " + id);
        }

        return reservaVuelo;
    }

    /**
     * Actualiza una reserva de habitación existente con la información proporcionada en el DTO de reserva de vuelo.
     *
     * @param id              El ID de la reserva de habitación que se va a actualizar.
     * @param reservaVueloDTO El DTO de reserva de vuelo que contiene la información actualizada.
     * @throws NoSuchElementException Si no se encuentra ninguna reserva de vuelo con el ID especificado.
     */
    @Override
    public void updateReservaVuelo(Long id, ReservaVueloDTO reservaVueloDTO) {
        ReservaVuelo reservaVuelo = reservaVueloRepository.findByIdAndBorradoFalse(id);

        if (reservaVuelo != null) {
            reservaVuelo.setEstado(reservaVueloDTO.getEstado());
            reservaVuelo.setFechaReserva(reservaVueloDTO.getFechaReserva());
            reservaVuelo.setPrecioTotal(reservaVueloDTO.getPrecioTotal());
            reservaVuelo.setCliente(reservaVueloDTO.getCliente());
            reservaVuelo.setVuelo(reservaVueloDTO.getVuelo());

            reservaVueloRepository.save(reservaVuelo);
        } else {
            throw new NoSuchElementException("No se encontró ninguna reserva con el ID " + id);
        }
    }

    /**
     * Convierte un objeto ReservaVuelo en un DTO (Data Transfer Object) de ReservaVuelo.
     *
     * @param reservaVuelo El objeto ReservaVuelo a convertir.
     * @return El DTO de ReservaVuelo generado.
     */
    public ReservaVueloDTO getReservaVuelo(ReservaVuelo reservaVuelo) {
        ReservaVueloDTO dto = new ReservaVueloDTO();
        dto.setEstado(reservaVuelo.getEstado());
        dto.setFechaReserva(reservaVuelo.getFechaReserva());
        dto.setPrecioTotal(reservaVuelo.getPrecioTotal());
        dto.setCliente(reservaVuelo.getCliente());
        dto.setVuelo(reservaVuelo.getVuelo());

        return dto;
    }

    /**
     * Guarda una reserva de vuelo si el vuelo asociado está disponible para la fecha especificada
     * y el cliente asociado a la reserva existe en el sistema.
     *
     * @param reservaVuelo La reserva de vuelo que se desea guardar.
     * @return La reserva de vuelo guardada, o null si el vuelo no está disponible o el cliente no existe.
     */
    @Override
    @Transactional
    public ReservaVuelo saveReservaVuelo(ReservaVuelo reservaVuelo) {
        // Verificar si el vuelo existe y no está borrado
        Vuelo vuelo = vueloService.findVuelo(reservaVuelo.getVuelo().getId());
        if (vuelo == null || vuelo.getBorrado()) {
            return null; // Vuelo no existe o está borrado
        }

        // Verificar si el cliente existe y no está borrado
        Cliente cliente = clienteService.findCliente(reservaVuelo.getCliente().getId());
        if (cliente == null || cliente.getBorrado()) {
            return null; // Cliente no existe o está borrado
        }

        // Verificar si la fecha de reserva es posterior a la fecha de partida del vuelo
        LocalDate fechaPartida = vuelo.getFechaPartida();
        if (fechaPartida != null && reservaVuelo.getFechaReserva().isAfter(fechaPartida)) {
            return null; // Fecha de reserva es posterior a la fecha de partida del vuelo
        }

        // Si pasa todas las validaciones, guardar la reserva de vuelo
        return reservaVueloRepository.save(reservaVuelo);
    }
}
