package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IHabitacionRepository;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabitacionService implements IHabitacionService {

    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Autowired
    private IHotelRepository hotelRepository;

    /**
     * Obtiene la lista de habitaciones que no han sido marcadas como borradas.
     *
     * @return La lista de DTOs (Data Transfer Objects) de las habitaciones encontradas.
     * @throws NoSuchElementException Si no se encuentran habitaciones que no estén marcadas como borradas.
     */
    @Override
    public List<HabitacionDTO> getHabitaciones() {
        List<Habitacion> habitaciones = habitacionRepository.findByBorradoFalse();
        if (habitaciones.isEmpty()) {
            throw new NoSuchElementException("No se encontraron habitaciones.");
        }

        return habitaciones.stream()
                .map(hotel -> getHabitacionDTO(hotel))
                .collect(Collectors.toList());
    }

    /**
     * Busca una habitación por su ID y asegura que no esté marcada como borrada.
     *
     * @param id El ID de la habitación a buscar.
     * @return La habitación encontrada.
     * @throws NoSuchElementException Si no se encuentra ninguna habitación con el ID proporcionado o si está marcada como borrada.
     */
    @Override
    public Habitacion findHabitacion(Long id) {
        Habitacion habitacion = habitacionRepository.findByIdAndBorradoFalse(id);

        if (habitacion == null) {
            throw new NoSuchElementException("No se encontró ninguna habitación con el ID " + id);
        }

        return habitacion;
    }

    /**
     * Guarda una habitación en la base de datos, si no existe previamente.
     *
     * @param habitacion La habitación a guardar.
     * @return La habitación guardada, si se ha guardado con éxito.
     * @throws IllegalArgumentException Si la habitación ya existe en la base de datos
     *                                  o si el hotel asociado no existe.
     */
    @Override
    public Habitacion saveHabitacion(Habitacion habitacion) {
        // Verificar si el ID de la habitación es nulo (indicando que es una nueva habitación)
        if (habitacion.getId() == null) {
            Long hotelId = habitacion.getHotel().getId();

            // Verificar si el hotel asociado existe por su ID
            Hotel existingHotel = hotelRepository.findById(hotelId).orElse(null);
            if (existingHotel == null) {
                throw new IllegalArgumentException("El hotel asociado con ID " + hotelId + " no existe en la base de datos.");
            }

            // Asociar la habitación con el hotel
            habitacion.setHotel(existingHotel);

            // Guardar la habitación en la base de datos
            return habitacionRepository.save(habitacion);
        } else {
            throw new IllegalArgumentException("La habitación ya tiene un ID asignado y no puede ser guardada como nueva.");
        }
    }



    /**
     * Elimina una habitación de la base de datos.
     *
     * @param id El ID de la habitación a eliminar.
     * @throws IllegalArgumentException Si no se encuentra ninguna habitación con el ID proporcionado.
     * @throws IllegalStateException    Si la habitación ya ha sido marcada como borrada.
     */
    @Override
    public void deleteHabitacion(Long id) {
        Optional<Habitacion> optionalHabitacion = habitacionRepository.findById(id);

        if (optionalHabitacion.isPresent()) {
            Habitacion habitacion = optionalHabitacion.get();

            if (habitacion.getBorrado()) {
                throw new IllegalStateException("La habitación con ID " + id + " ya ha sido borrado.");
            } else {
                habitacion.setBorrado(true);
                habitacionRepository.save(habitacion);
            }
        } else {
            throw new IllegalArgumentException("No se encontró ninguna habitación con ID " + id + ".");
        }
    }

    /**
     * Actualiza una habitación en la base de datos.
     *
     * @param id            El ID de la habitación a actualizar.
     * @param habitacionDTO La información actualizada de la habitación.
     * @throws NoSuchElementException Si no se encuentra ninguna habitación con el ID proporcionado.
     */
    @Override
    public void updateHabitacion(Long id, HabitacionDTO habitacionDTO) {
        Habitacion habitacion = habitacionRepository.findByIdAndBorradoFalse(id);

        if (habitacion != null) {
            habitacion.setNumeroHabitacion(habitacionDTO.getNumeroHabitacion());
            habitacion.setCapacidad(habitacionDTO.getCapacidad());
            habitacion.setTipoHabitacion(habitacionDTO.getTipoHabitacion());
            habitacion.setEstado(habitacionDTO.getEstado());
            habitacion.setPrecioDia(habitacionDTO.getPrecioDia());
            habitacion.setDescripcion(habitacionDTO.getDescripcion());
            habitacion.setDisponibleDesde(habitacionDTO.getDisponibleDesde());
            habitacion.setDisponibleHasta(habitacionDTO.getDisponibleHasta());
            habitacion.setHotel(habitacionDTO.getHotel());
            habitacion.setListaReservas(habitacionDTO.getListaReservas());

            habitacionRepository.save(habitacion);
        } else {
            throw new NoSuchElementException("No se encontró ninguna habitación con el ID " + id);
        }
    }

    /**
     * Convierte un objeto Habitación en un DTO (Data Transfer Object) de Habitación.
     *
     * @param habitacion El objeto Habitación a convertir.
     * @return El DTO de Habitación generado.
     */
    public HabitacionDTO getHabitacionDTO(Habitacion habitacion) {
        HabitacionDTO habitacionDTO = new HabitacionDTO();
        habitacionDTO.setNumeroHabitacion(habitacion.getNumeroHabitacion());
        habitacionDTO.setCapacidad(habitacion.getCapacidad());
        habitacionDTO.setTipoHabitacion(habitacion.getTipoHabitacion());
        habitacionDTO.setEstado(habitacion.getEstado());
        habitacionDTO.setPrecioDia(habitacion.getPrecioDia());
        habitacionDTO.setDescripcion(habitacionDTO.getDescripcion());
        habitacionDTO.setDisponibleDesde(habitacion.getDisponibleDesde());
        habitacionDTO.setDisponibleHasta(habitacion.getDisponibleHasta());
        habitacionDTO.setHotel(habitacion.getHotel());
        habitacionDTO.setListaReservas(habitacion.getListaReservas());

        return habitacionDTO;
    }
}
