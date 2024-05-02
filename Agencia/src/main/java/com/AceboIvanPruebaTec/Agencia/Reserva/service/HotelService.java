package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HotelDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HotelService implements IHotelService {

    @Autowired
    private IHotelRepository hotelRepository;

    /**
     * Guarda un hotel en la base de datos. Si el hotel no tiene un código asignado, genera uno automáticamente.
     * Si se proporciona un código, se verifica si ya existe un hotel con ese código.
     *
     * @param hotel El hotel a guardar en la base de datos.
     * @return El hotel guardado, o null si ya existe un hotel con el mismo código.
     */
    @Override
    public Hotel saveHotel(Hotel hotel) {
        if (hotel.getCodigo() == null || hotel.getCodigo().isEmpty()) {
            hotel.setCodigo(codigoHotel());
        } else {
            Hotel existingHotel = hotelRepository.findByCodigo(hotel.getCodigo());
            if (existingHotel != null) {
                throw new IllegalStateException("El hotel con el código " + hotel.getCodigo() + " ya existe.");
            }
        }

        return hotelRepository.save(hotel);
    }


    /**
     * Obtiene la lista de hoteles que no han sido marcados como borrados.
     *
     * @return La lista de DTOs (Data Transfer Objects) de los hoteles encontrados.
     * @throws NoSuchElementException Si no se encuentran hoteles que no estén marcados como borrados.
     */
    @Override
    public List<HotelDTO> getHoteles() {
        List<Hotel> hoteles = hotelRepository.findNotDeletedHotels();

        if (hoteles.isEmpty()) {
            throw new NoSuchElementException("No se encontraron hoteles.");
        }

        return hoteles.stream()
                .map(hotel -> getHotelDTO(hotel))
                .collect(Collectors.toList());
    }

    /**
     * Elimina un hotel de la base de datos por su ID, verificando primero si alguna de sus habitaciones tiene reservas activas.
     *
     * @param id El ID del hotel a eliminar.
     * @throws IllegalArgumentException Si no se encuentra ningún hotel con el ID proporcionado.
     * @throws IllegalStateException    Si alguna habitación del hotel tiene reservas activas, impidiendo la eliminación del hotel.
     */
    @Override
    public void deleteHotel(Long id) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);

        if (optionalHotel.isPresent()) {
            Hotel hotel = optionalHotel.get();

            boolean hasReservations = hotel.getHabitaciones().stream()
                    .flatMap(habitacion -> habitacion.getListaReservas().stream())
                    .anyMatch(reserva -> !reserva.getBorrado());

            if (hasReservations) {
                throw new IllegalStateException("No se puede eliminar el hotel con ID " + id + " porque tiene habitaciones con reservas.");
            } else {
                if (!hotel.getBorrado()) {
                    hotel.setBorrado(true);
                    hotelRepository.save(hotel);
                } else {
                    throw new IllegalStateException("El hotel con ID " + id + " ya ha sido borrado.");
                }
            }
        } else {
            throw new IllegalArgumentException("No se encontró ningún hotel con ID " + id + ".");
        }
    }

    /**
     * Encuentra un hotel en la base de datos por su ID, asegurándose de que no esté marcado como borrado.
     *
     * @param id El ID del hotel a buscar.
     * @return El hotel encontrado.
     * @throws NoSuchElementException Si no se encuentra ningún hotel con el ID proporcionado.
     */
    @Override
    public Hotel findHotel(Long id) {
        Hotel hotel = hotelRepository.findByIdAndBorradoFalse(id);

        if (hotel == null) {
            throw new NoSuchElementException("No se encontró ningún hotel con el ID " + id);
        }

        return hotel;
    }

    /**
     * Actualiza un hotel en la base de datos con la información proporcionada en el DTO.
     *
     * @param id       El ID del hotel a actualizar.
     * @param hotelDTO El DTO con la información actualizada del hotel.
     * @throws NoSuchElementException Si no se encuentra ningún hotel con el ID proporcionado.
     */
    @Override
    public void updateHotel(Long id, HotelDTO hotelDTO) {
        Hotel hotel = hotelRepository.findByIdAndBorradoFalse(id);

        if (hotel != null) {
            hotel.setCodigo(hotelDTO.getCodigo());
            hotel.setNombre(hotelDTO.getNombre());
            hotel.setPais(hotelDTO.getPais());
            hotel.setCiudad(hotelDTO.getCiudad());
            hotel.setDireccion(hotelDTO.getDireccion());
            hotel.setEmail(hotelDTO.getEmail());
            hotel.setNumeroTelefono(hotelDTO.getNumeroTelefono());
            hotel.setHabitaciones(hotelDTO.getHabitaciones());

            hotelRepository.save(hotel);
        } else {
            throw new NoSuchElementException("No se encontró ningún hotel con el ID " + id);
        }
    }

    /**
     * Obtiene las habitaciones disponibles para un rango de fechas y un país específico.
     *
     * @param disponibleDesde Fecha de inicio del rango de disponibilidad.
     * @param disponibleHasta Fecha de fin del rango de disponibilidad.
     * @param pais            País donde se encuentran los hoteles.
     * @return Lista de habitaciones disponibles que cumplen con los criterios de búsqueda.
     * @throws NoSuchElementException Si no se encuentran habitaciones disponibles para los criterios de búsqueda.
     */
    @Override
    public List<Habitacion> getHabitacionesFiltro(LocalDate disponibleDesde, LocalDate disponibleHasta, String pais) {
        List<Hotel> hoteles = hotelRepository.findNotDeletedHotels().stream()
                .filter(hotel -> hotel.getPais().equalsIgnoreCase(pais))
                .collect(Collectors.toList());

        if (hoteles.isEmpty()) {
            throw new NoSuchElementException("No se encontraron hoteles disponibles en el país especificado.");
        }

        List<Habitacion> habitaciones = hoteles.stream()
                .flatMap(hotel -> hotel.getHabitaciones().stream())
                .filter(habitacion -> habitacion.getEstado().equalsIgnoreCase("Disponible"))
                .filter(habitacion -> habitacion.getDisponibleDesde().isBefore(disponibleHasta)
                        && habitacion.getDisponibleHasta().isAfter(disponibleDesde))
                .collect(Collectors.toList());

        if (habitaciones.isEmpty()) {
            throw new NoSuchElementException("No se encontraron habitaciones disponibles para los criterios de búsqueda.");
        }

        return habitaciones;
    }


    /**
     * Genera un código único para un hotel si no se proporciona uno.
     *
     * @return El código generado para el hotel.
     */
    private String codigoHotel() {
        return "HO-" + UUID.randomUUID().toString();
    }

    /**
     * Convierte un objeto Hotel en un DTO (Data Transfer Object) de Hotel.
     *
     * @param hotel El objeto Hotel a convertir.
     * @return El DTO de Hotel generado.
     */
    public HotelDTO getHotelDTO(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setCodigo(hotel.getCodigo());
        hotelDTO.setNombre(hotel.getNombre());
        hotelDTO.setPais(hotel.getPais());
        hotelDTO.setCiudad(hotel.getCiudad());
        hotelDTO.setDireccion(hotel.getDireccion());
        hotelDTO.setEmail(hotel.getEmail());
        hotelDTO.setNumeroTelefono(hotel.getNumeroTelefono());
        hotelDTO.setHabitaciones(hotel.getHabitaciones());

        return hotelDTO;
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
