package com.AceboIvanPruebaTec.Agencia.Reserva.controller;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HotelDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IHotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/agency/hotels")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    @Operation(summary = "Dar de alta un hotel")
    @PostMapping("/new")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de hotel no válidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al guardar el hotel")
    })
    public ResponseEntity<?> saveHotel(@RequestBody Hotel hotel) {
        try {
            hotelService.saveHotel(hotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de hotel no válidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el hotel: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar todos los hoteles")
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de hoteles encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay hoteles disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener los hoteles")
    })
    public ResponseEntity<?> getHoteles() {
        try {
            List<HotelDTO> hoteles = hotelService.getHoteles();

            if (hoteles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay hoteles disponibles.");
            } else {
                return ResponseEntity.ok(hoteles);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los hoteles: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar un hotel por su ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún hotel con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el hotel")
    })
    public ResponseEntity<?> getHotel(@PathVariable Long id) {
        try {
            Hotel hotel = hotelService.findHotel(id);

            if (hotel == null) {
                return ResponseEntity.status((HttpStatus.NOT_FOUND)).body("No se encontró ningún hotel con el ID " + id);
            } else {
                return ResponseEntity.ok(hotel);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el hotel: " + e.getMessage());
        }
    }

    @Operation(summary = "Borrado lógico de un hotel por su ID")
    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel borrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún hotel con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "El hotel ya está marcado como borrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al eliminar el hotel")
    })
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {
        try {
            hotelService.deleteHotel(id);
            return ResponseEntity.status(HttpStatus.OK).body("Hotel borrado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún hotel con el ID proporcionado.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El hotel con ID " + id + " ya está marcado como borrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al intentar borrar el hotel: " + e.getMessage());
        }
    }

    @Operation(summary = "Modificación de los datos de un hotel por su ID")
    @PutMapping("/edit/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún hotel con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "Error al editar el hotel"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al editar el hotel")
    })
    public ResponseEntity<?> editHotel(@PathVariable Long id, @RequestBody HotelDTO updateHotel) {
        try {
            hotelService.updateHotel(id, updateHotel);
            return ResponseEntity.ok("Hotel con ID " + id + " actualizado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al editar el hotel: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar habitaciones disponibles por país y rango de fechas")
    @GetMapping("/available-rooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de habitaciones disponibles"),
            @ApiResponse(responseCode = "204", description = "No hay habitaciones disponibles para los criterios de búsqueda"),
            @ApiResponse(responseCode = "404", description = "No se encontraron habitaciones disponibles para los criterios de búsqueda"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener las habitaciones disponibles")
    })
    public ResponseEntity<?> getAvailableHabitaciones(
            @RequestParam("disponibleDesde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate disponibleDesde,
            @RequestParam("disponibleHasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate disponibleHasta,
            @RequestParam("pais") String pais) {
        try {
            List<Habitacion> habitaciones = hotelService.getHabitacionesFiltro(disponibleDesde, disponibleHasta, pais);

            if (habitaciones.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay habitaciones disponibles para los criterios de búsqueda.");
            } else {
                return ResponseEntity.ok(habitaciones);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron habitaciones disponibles para los criterios de búsqueda.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las habitaciones disponibles: " + e.getMessage());
        }
    }
}
