package com.AceboIvanPruebaTec.Agencia.Reserva.controller;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ReservaHabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaHabitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IReservaHabitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/agency/hotel-booking")
public class ReservaHabitacionController {

    @Autowired
    private IReservaHabitacionService reservaHabitacionService;

    @Operation(summary = "Obtener todas las reservas de habitaciones activas")
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas de habitaciones activas"),
            @ApiResponse(responseCode = "204", description = "No hay reservas de habitaciones activas"),
            @ApiResponse(responseCode = "500", description = "Error al obtener las reservas de habitaciones")
    })
    public ResponseEntity<?> getReservasHabitaciones() {
        try {
            List<ReservaHabitacionDTO> reservas = reservaHabitacionService.getReservasHabitaciones();

            if (reservas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay reservas de habitaciones activas.");
            } else {
                return ResponseEntity.ok(reservas);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron reservas de habitaciones activas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las reservas de habitaciones: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener una reserva de habitación por su ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva de habitación encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reserva de habitación con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error al buscar la reserva de habitación")
    })
    public ResponseEntity<?> findReservaHabitacionById(@PathVariable Long id) {
        try {
            ReservaHabitacion reserva = reservaHabitacionService.findReservaHabitacion(id);
            return ResponseEntity.ok(reserva);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna reserva de habitación con el ID proporcionado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la reserva de habitación: " + e.getMessage());
        }
    }

    @Operation(summary = "Guardar una reserva de habitación")
    @PostMapping("/new")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva de habitación guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al guardar la reserva de habitación"),
            @ApiResponse(responseCode = "500", description = "Error al guardar la reserva de habitación")
    })
    public ResponseEntity<?> saveReservaHabitacion(@RequestBody ReservaHabitacion reservaHabitacion) {
        try {
            ReservaHabitacion savedReserva = reservaHabitacionService.saveReservaHabitacion(reservaHabitacion);

            if (savedReserva != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedReserva);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La habitación no está disponible para las fechas especificadas.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la reserva de habitación: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una reserva de habitación por su ID")
    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva de habitación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reserva de habitación con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la reserva de habitación"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la reserva de habitación")
    })
    public ResponseEntity<?> deleteReservaHabitacion(@PathVariable Long id) {
        try {
            ReservaHabitacion deletedReserva = reservaHabitacionService.deleteReservaHabitacion(id);

            if (deletedReserva != null) {
                return ResponseEntity.ok("Reserva de habitación con ID " + id + " eliminada correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna reserva de habitación con el ID proporcionado.");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la reserva de habitación: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar una reserva de habitación por su ID")
    @PutMapping("/edit/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva de habitación actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna reserva de habitación con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar la reserva de habitación")
    })
    public ResponseEntity<?> updateReservaHabitacion(@PathVariable Long id, @RequestBody ReservaHabitacionDTO reservaHabitacionDTO) {
        try {
            reservaHabitacionService.updateReservaHabitacion(id, reservaHabitacionDTO);
            return ResponseEntity.ok("Reserva de habitación con ID " + id + " actualizada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna reserva de habitación con el ID proporcionado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la reserva de habitación: " + e.getMessage());
        }
    }
}
