package com.AceboIvanPruebaTec.Agencia.Reserva.controller;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IHabitacionService;
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
@RequestMapping("/agency/hotels/rooms")
public class HabitacionController {

    @Autowired
    private IHabitacionService habitacionService;

    @Operation(summary = "Buscar todas las habitaciones")
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de habitaciones encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay habitaciones disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener las habitaciones")
    })
    public ResponseEntity<?> getHabitaciones() {
        try {
            List<HabitacionDTO> habitaciones = habitacionService.getHabitaciones();

            if (habitaciones.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay habitaciones disponibles.");
            } else {
                return ResponseEntity.ok(habitaciones);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron habitaciones disponibles.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las habitaciones: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar una habitación por su ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitación encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna habitación con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener la habitación")
    })
    public ResponseEntity<?> getHabitacionId(@PathVariable Long id) {
        try {
            Habitacion habitacion = habitacionService.findHabitacion(id);

            if (habitacion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna habitación con el ID " + id);
            } else {
                return ResponseEntity.ok(habitacion);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna habitación con el ID " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la habitación: " + e.getMessage());
        }
    }

    @Operation(summary = "Guardar una nueva habitación")
    @PostMapping("/new")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Habitación guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al guardar la habitación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al guardar la habitación")
    })
    public ResponseEntity<?> saveHabitacion(@RequestBody Habitacion habitacion) {
        try {
            Habitacion savedHabitacion = habitacionService.saveHabitacion(habitacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedHabitacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar la habitación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la habitación: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una habitación por su ID")
    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna habitación con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la habitación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al eliminar la habitación")
    })
    public ResponseEntity<?> deleteHabitacion(@PathVariable Long id) {
        try {
            habitacionService.deleteHabitacion(id);
            return ResponseEntity.ok("Habitación con ID " + id + " eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna habitación con ID " + id + ".");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la habitación: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar una habitación por su ID")
    @PutMapping("/edit/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitación actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna habitación con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar la habitación")
    })
    public ResponseEntity<?> updateHabitacion(@PathVariable Long id, @RequestBody HabitacionDTO habitacionDTO) {
        try {
            habitacionService.updateHabitacion(id, habitacionDTO);
            return ResponseEntity.ok("Habitación con ID " + id + " actualizada correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna habitación con ID " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la habitación: " + e.getMessage());
        }
    }
}