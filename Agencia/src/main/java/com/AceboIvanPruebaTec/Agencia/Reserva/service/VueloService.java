package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.VueloDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IVueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VueloService implements IVueloService {

    @Autowired
    private IVueloRepository vueloRepository;

    /**
     * Obtiene la lista de vuelos disponibles que no han sido marcados como borrados.
     *
     * @return La lista de DTOs (Data Transfer Objects) de los vuelos encontrados.
     * @throws NoSuchElementException Si no se encuentran vuelos disponibles.
     */
    @Override
    public List<VueloDTO> getVuelos() {
        List<Vuelo> vuelos = vueloRepository.findByBorradoFalse();

        if (vuelos.isEmpty()) {
            throw new NoSuchElementException("No se encontraron Vuelos.");
        }

        return vuelos.stream()
                .map(hotel -> getVueloDTO(hotel))
                .collect(Collectors.toList());
    }

    /**
     * Guarda un vuelo en el repositorio.
     * Si el vuelo proporcionado es nulo, se lanza una excepción IllegalArgumentException.
     * Antes de guardar el vuelo, se verifica si ya existe un vuelo con el mismo código.
     * Si el código no se proporciona o está vacío, se genera uno nuevo.
     * Si el vuelo ya existe en la base de datos, se lanza una excepción IllegalStateException.
     *
     * @param vuelo El vuelo que se va a guardar.
     * @return El vuelo guardado si se realiza con éxito.
     * @throws IllegalArgumentException Si el vuelo proporcionado es nulo.
     * @throws IllegalStateException    Si el vuelo ya existe en la base de datos.
     */
    @Override
    public Vuelo saveVuelo(Vuelo vuelo) {
        if (vuelo == null) {
            throw new IllegalArgumentException("El vuelo proporcionado es nulo.");
        }

        if (vuelo.getCodigo() == null || vuelo.getCodigo().isEmpty()) {
            vuelo.setCodigo(codigoVuelo());
        } else {
            Vuelo existingVuelo = vueloRepository.findByCodigo(vuelo.getCodigo());
            if (existingVuelo != null) {
                throw new IllegalStateException("El vuelo con el código " + vuelo.getCodigo() + " ya existe.");
            }
        }

        return vueloRepository.save(vuelo);
    }

    /**
     * Elimina un vuelo de la base de datos por su ID.
     * Verifica si el vuelo tiene reservas activas antes de eliminarlo.
     *
     * @param id El ID del vuelo a eliminar.
     * @throws IllegalArgumentException Si no se encuentra ningún vuelo con el ID proporcionado.
     * @throws IllegalStateException    Si el vuelo tiene reservas activas o si ya ha sido marcado como borrado.
     */

    @Override
    public void deleteVuelo(Long id) {
        Optional<Vuelo> optionalVuelo = vueloRepository.findById(id);

        if (optionalVuelo.isPresent()) {
            Vuelo vuelo = optionalVuelo.get();

            boolean hasActiveReservations = vuelo.getListaReservas().stream()
                    .anyMatch(reserva -> !reserva.getBorrado());

            if (hasActiveReservations) {
                throw new IllegalStateException("No se puede eliminar el vuelo con ID " + id + " porque tiene reservas activas.");
            } else {
                if (!vuelo.getBorrado()) {
                    vuelo.setBorrado(true);
                    vueloRepository.save(vuelo);
                } else {
                    throw new IllegalStateException("El vuelo con ID " + id + " ya ha sido borrado.");
                }
            }
        } else {
            throw new IllegalArgumentException("No se encontró ningún vuelo con ID " + id + ".");
        }
    }

    /**
     * Busca un vuelo por su ID y asegura que no esté marcado como borrado.
     *
     * @param id El ID del vuelo a buscar.
     * @return El vuelo encontrado.
     * @throws NoSuchElementException Si no se encuentra ningún vuelo con el ID proporcionado o si está marcado como borrado.
     */
    @Override
    public Vuelo findVuelo(Long id) {
        Vuelo vuelo = vueloRepository.findByIdAndBorradoFalse(id);

        if (vuelo == null) {
            throw new NoSuchElementException("No se encontró ningún vuelo con el ID " + id);
        }

        return vuelo;
    }

    /**
     * Actualiza un vuelo en la base de datos con la información proporcionada en un objeto VueloDTO.
     *
     * @param id       El ID del vuelo que se va a actualizar.
     * @param vueloDTO El objeto VueloDTO que contiene la información actualizada del vuelo.
     * @throws NoSuchElementException Si no se encuentra ningún vuelo con el ID proporcionado.
     */
    @Override
    public void updateVuelo(Long id, VueloDTO vueloDTO) {
        Vuelo vuelo = vueloRepository.findByIdAndBorradoFalse(id);

        if (vuelo != null) {
            vuelo.setCodigo(vueloDTO.getCodigo());
            vuelo.setTipoAsiento(vueloDTO.getTipoAsiento());
            vuelo.setCantidadAsientos(vueloDTO.getCantidadAsientos());
            vuelo.setOrigen(vueloDTO.getOrigen());
            vuelo.setDestino(vueloDTO.getDestino());
            vuelo.setFechaPartida(vueloDTO.getFechaPartida());
            vuelo.setFechaLlegada(vueloDTO.getFechaLlegada());
            vuelo.setMillas(vueloDTO.getMillas());
            vuelo.setEsDirecto(vueloDTO.getEsDirecto());
            vuelo.setAerolinea(vueloDTO.getAerolinea());

            vueloRepository.save(vuelo);
        } else {
            throw new NoSuchElementException("No se encontró ningún vuelo con el ID " + id);
        }
    }


    /**
     * Obtiene una lista de vuelos filtrados según los criterios de origen, destino, fecha de salida y fecha de llegada.
     *
     * @param origen       El origen del vuelo.
     * @param destino      El destino del vuelo.
     * @param fechaSalida  La fecha de salida del vuelo.
     * @param fechaLlegada La fecha de llegada del vuelo.
     * @return Una lista de vuelos que coinciden con los criterios de filtro.
     */
    @Override
    public List<Vuelo> getVuelosFiltro(String origen, String destino, LocalDate fechaSalida, LocalDate fechaLlegada) {
        List<Vuelo> vuelos = vueloRepository.findByBorradoFalse();

        return vuelos.stream()
                .filter(vuelo ->
                        vuelo.getOrigen().equalsIgnoreCase(origen) &&
                                vuelo.getDestino().equalsIgnoreCase(destino) &&
                                vuelo.getFechaPartida().isEqual(fechaSalida) &&
                                vuelo.getFechaLlegada().isEqual(fechaLlegada))
                .collect(Collectors.toList());
    }


    /**
     * Genera un código único para un vuelo si no se proporciona uno.
     *
     * @return El código generado para el vuelo.
     */
    private String codigoVuelo() {
        return "AV-" + UUID.randomUUID().toString();
    }

    /**
     * Convierte un objeto Vuelo en un DTO (Data Transfer Object) de Vuelo.
     *
     * @param vuelo El objeto vuelo a convertir.
     * @return El DTO de vuelo generado.
     */
    public VueloDTO getVueloDTO(Vuelo vuelo) {
        VueloDTO vueloDTO = new VueloDTO();
        vueloDTO.setCodigo(vuelo.getCodigo());
        vueloDTO.setTipoAsiento(vuelo.getTipoAsiento());
        vueloDTO.setCantidadAsientos(vuelo.getCantidadAsientos());
        vueloDTO.setOrigen(vuelo.getOrigen());
        vueloDTO.setDestino(vuelo.getDestino());
        vueloDTO.setFechaPartida(vuelo.getFechaPartida());
        vueloDTO.setFechaLlegada(vuelo.getFechaLlegada());
        vueloDTO.setMillas(vuelo.getMillas());
        vueloDTO.setEsDirecto(vuelo.getEsDirecto());
        vueloDTO.setAerolinea(vuelo.getAerolinea());
        vueloDTO.setListaReservas(vuelo.getListaReservas());

        return vueloDTO;
    }
}
