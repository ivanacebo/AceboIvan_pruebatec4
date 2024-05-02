package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ReservaHabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Cliente;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaHabitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IReservaHabitacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaHabitacionService implements IReservaHabitacionService {

    @Autowired
    private IReservaHabitacionRepository reservaHabitacionRepository;

    @Autowired
    private IHabitacionService habitacionService;

    @Autowired
    private ClienteService clienteService;

    /**
     * Obtiene una lista de todas las reservas de habitaciones activas.
     *
     * @return Lista de reservas de habitaciones activas.
     * @throws NoSuchElementException Si no se encuentran reservas de habitaciones activas.
     */
    @Override
    public List<ReservaHabitacionDTO> getReservasHabitaciones() {
        List<ReservaHabitacion> reservaHabitaciones = reservaHabitacionRepository.findNotDeletedReservasHabitacion();

        if (reservaHabitaciones.isEmpty()) {
            throw new NoSuchElementException("No se encontraron reservas para habitaciones.");
        }

        return reservaHabitaciones.stream()
                .map(reservaHabitacion -> getReservaHabitacion(reservaHabitacion))
                .collect(Collectors.toList());
    }

    /**
     * Elimina lógicamente una reserva de habitación mediante su identificador.
     * Si la reserva de habitación se encuentra y no ha sido marcada como borrada previamente,
     * se marca como borrada y se guarda en el repositorio. Si la reserva de habitación ya ha sido
     * marcada como borrada, se lanza una excepción.
     *
     * @param id El identificador de la reserva de habitación a eliminar.
     * @return La reserva de habitación marcada como borrada si se realiza con éxito, de lo contrario null.
     * @throws IllegalStateException Si la reserva de habitación ya ha sido marcada como borrada anteriormente.
     */
    @Override
    public ReservaHabitacion deleteReservaHabitacion(Long id) {
        Optional<ReservaHabitacion> optionalReservaHabitacion = reservaHabitacionRepository.findById(id);
        if (optionalReservaHabitacion.isPresent()) {
            ReservaHabitacion reservaHabitacion = optionalReservaHabitacion.get();
            if (reservaHabitacion.getBorrado()) {
                throw new IllegalStateException("La reserva de habitación con ID " + id + " ya ha sido borrada.");
            } else {
                reservaHabitacion.setBorrado(true);
                return reservaHabitacionRepository.save(reservaHabitacion);
            }
        } else {
            return null;
        }
    }

    /**
     * Busca una reserva de habitación por su ID en el repositorio.
     *
     * @param id El ID de la reserva de habitación a buscar.
     * @return La reserva de habitación encontrada si existe y no está marcada como borrada.
     * @throws NoSuchElementException Si no se encuentra ninguna reserva de habitación con el ID proporcionado.
     */
    @Override
    public ReservaHabitacion findReservaHabitacion(Long id) {
        ReservaHabitacion reservaHabitacion = reservaHabitacionRepository.findByIdAndBorradoFalse(id);

        if (reservaHabitacion == null) {
            throw new NoSuchElementException("No se encontró ninguna reserva de hotel con el ID " + id);
        }

        return reservaHabitacion;
    }

    /**
     * Actualiza una reserva de habitación existente en el sistema con la información proporcionada en un DTO.
     *
     * @param id                   El ID de la reserva de habitación que se va a actualizar.
     * @param reservaHabitacionDTO El DTO que contiene la información actualizada de la reserva de habitación.
     * @throws NoSuchElementException Si no se encuentra ninguna reserva de habitación con el ID especificado.
     */
    @Override
    public void updateReservaHabitacion(Long id, ReservaHabitacionDTO reservaHabitacionDTO) {
        ReservaHabitacion reservaHabitacion = reservaHabitacionRepository.findByIdAndBorradoFalse(id);

        if (reservaHabitacion != null) {
            reservaHabitacion.setEstado(reservaHabitacionDTO.getEstado());
            reservaHabitacion.setFechaReserva(reservaHabitacionDTO.getFechaReserva());
            reservaHabitacion.setPrecioTotal(reservaHabitacionDTO.getPrecioTotal());
            reservaHabitacion.setCliente(reservaHabitacionDTO.getCliente());
            reservaHabitacion.setHabitacion(reservaHabitacionDTO.getHabitacion());

            reservaHabitacionRepository.save(reservaHabitacion);
        } else {
            throw new NoSuchElementException("No se encontró ninguna reserva con el ID " + id);
        }

    }

    /**
     * Convierte un objeto ReservaHabitacionDTO en un DTO (Data Transfer Object) de ReservaHabitacion.
     *
     * @param reservaHabitacion El objeto ReservaHabitacionDTO a convertir.
     * @return El DTO de ReservaHabitacion generado.
     */
    public ReservaHabitacionDTO getReservaHabitacion(ReservaHabitacion reservaHabitacion) {
        ReservaHabitacionDTO dto = new ReservaHabitacionDTO();
        dto.setEstado(reservaHabitacion.getEstado());
        dto.setFechaReserva(reservaHabitacion.getFechaReserva());
        dto.setPrecioTotal(reservaHabitacion.getPrecioTotal());
        dto.setCliente(reservaHabitacion.getCliente());
        dto.setHabitacion(reservaHabitacion.getHabitacion());

        return dto;
    }


    /**
     * Este método guarda una reserva de habitación en la base de datos.
     * Verifica que la habitación y el cliente asociados a la reserva existan y no estén marcados como borrados.
     * Si alguna de estas condiciones no se cumple, se lanza una excepción correspondiente.
     * Finalmente, se guarda la reserva de habitación en el repositorio y se devuelve el objeto guardado.
     *
     * @param reservaHabitacion La reserva de habitación a guardar.
     * @return La reserva de habitación guardada.
     * @throws NoSuchElementException Si la habitación no existe o está borrada, o si el cliente no existe o está borrado.
     */
    @Override
    @Transactional
    public ReservaHabitacion saveReservaHabitacion(ReservaHabitacion reservaHabitacion) {
        Habitacion habitacion = reservaHabitacion.getHabitacion();

        if (habitacion == null || habitacion.getBorrado()) {
            throw new NoSuchElementException("La habitación no existe o está borrada.");
        }

        Cliente cliente = reservaHabitacion.getCliente();
        if (cliente == null || cliente.getBorrado()) {
            throw new NoSuchElementException("El cliente no existe o está borrado.");
        }

        return reservaHabitacionRepository.save(reservaHabitacion);
    }




}



