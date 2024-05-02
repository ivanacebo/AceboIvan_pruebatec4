package com.AceboIvanPruebaTec.Agencia.Reserva.controller;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ReservaVueloDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaVuelo;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IReservaVueloService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("agency/flight-booking")
public class ReservaVueloController {

    @Autowired
    private IReservaVueloService reservaVueloService;

    @Operation(summary = "Obtener todas las reservas de vuelos activas")
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas de vuelos activas"),
            @ApiResponse(responseCode = "204", description = "No hay reservas de vuelos activas"),
            @ApiResponse(responseCode = "500", description = "Error al obtener las reservas de vuelos")
    })
    public ResponseEntity<?> getReservaVuelos() {
        try {
            List<ReservaVueloDTO> reservas = reservaVueloService.getReservaVuelos();

            if (reservas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay reservas de vuelos activas.");
            } else {
                return ResponseEntity.ok(reservas);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron reservas de vuelos activas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las reservas de vuelos: " + e.getMessage());
        }
    }

    @Operation(summary = "Guardar una reserva de vuelo")
    @PostMapping("/new")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva de vuelo guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "El vuelo no está disponible para la fecha especificada o el cliente no existe"),
            @ApiResponse(responseCode = "500", description = "Error al guardar la reserva de vuelo")
    })
    public ResponseEntity<?> saveReservaVuelo(@RequestBody ReservaVuelo reservaVuelo) {
        try {
            ReservaVuelo savedReserva = reservaVueloService.saveReservaVuelo(reservaVuelo);

            if (savedReserva != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedReserva);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El vuelo no está disponible para la fecha especificada o el cliente no existe.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la reserva de vuelo: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una reserva de vuelo por su ID")
    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva de vuelo eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reserva de vuelo con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la reserva de vuelo")
    })
    public ResponseEntity<?> deleteReservaVuelo(@PathVariable Long id) {
        try {
            ReservaVuelo deletedReserva = reservaVueloService.deleteReservaVuelo(id);

            if (deletedReserva != null) {
                return ResponseEntity.ok("Reserva de vuelo con ID " + id + " eliminada correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna reserva de vuelo con el ID proporcionado.");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la reserva de vuelo: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar una reserva de vuelo por su ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva de vuelo encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reserva de vuelo con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error al buscar la reserva de vuelo")
    })
    public ResponseEntity<?> findReservaVueloById(@PathVariable Long id) {
        try {
            ReservaVuelo reserva = reservaVueloService.findReservaVuelo(id);
            return ResponseEntity.ok(reserva);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna reserva de vuelo con el ID proporcionado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la reserva de vuelo: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar una reserva de vuelo por su ID")
    @PutMapping("/edit/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva de vuelo actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reserva de vuelo con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar la reserva de vuelo")
    })
    public ResponseEntity<?> updateReservaVuelo(@PathVariable Long id, @RequestBody ReservaVueloDTO reservaVueloDTO) {
        try {
            reservaVueloService.updateReservaVuelo(id, reservaVueloDTO);
            return ResponseEntity.ok("Reserva de vuelo con ID " + id + " actualizada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna reserva de vuelo con el ID proporcionado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la reserva de vuelo: " + e.getMessage());
        }
    }
}

