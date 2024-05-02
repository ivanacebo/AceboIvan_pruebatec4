package com.AceboIvanPruebaTec.Agencia.Reserva.controller;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.VueloDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IVueloService;
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
@RequestMapping("/agency/flights")
public class VueloController {

    @Autowired
    private IVueloService vueloService;

    @Operation(summary = "Obtener vuelos filtrados")
    @GetMapping("/filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos filtrados"),
            @ApiResponse(responseCode = "204", description = "No hay vuelos que coincidan con los criterios de filtro"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtro inválidos"),
            @ApiResponse(responseCode = "500", description = "Error al obtener los vuelos filtrados")
    })
    public ResponseEntity<?> getVuelosFiltro(
            @RequestParam(value = "origen") String origen,
            @RequestParam(value = "destino") String destino,
            @RequestParam(value = "fechaSalida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida,
            @RequestParam(value = "fechaLlegada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaLlegada) {
        try {
            // Validar parámetros de filtro
            if (origen == null || destino == null || fechaSalida == null || fechaLlegada == null) {
                return ResponseEntity.badRequest().body("Parámetros de filtro inválidos.");
            }

            // Obtener vuelos filtrados
            List<Vuelo> vuelosFiltrados = vueloService.getVuelosFiltro(origen, destino, fechaSalida, fechaLlegada);

            if (vuelosFiltrados.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay vuelos que coincidan con los criterios de filtro.");
            } else {
                return ResponseEntity.ok(vuelosFiltrados);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los vuelos filtrados: " + e.getMessage());
        }
    }


    @Operation(summary = "Obtener todos los vuelos disponibles")
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos disponibles"),
            @ApiResponse(responseCode = "204", description = "No hay vuelos disponibles"),
            @ApiResponse(responseCode = "500", description = "Error al obtener los vuelos")
    })
    public ResponseEntity<?> getVuelos() {
        try {
            List<VueloDTO> vuelos = vueloService.getVuelos();

            if (vuelos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay vuelos disponibles.");
            } else {
                return ResponseEntity.ok(vuelos);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron vuelos disponibles.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los vuelos: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener un vuelo por su ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún vuelo con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error al obtener el vuelo")
    })
    public ResponseEntity<?> getVueloById(@PathVariable Long id) {
        try {
            Vuelo vuelo = vueloService.findVuelo(id);

            if (vuelo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún vuelo con el ID " + id);
            } else {
                return ResponseEntity.ok(vuelo);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún vuelo con el ID " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el vuelo: " + e.getMessage());
        }
    }

    @Operation(summary = "Guardar un nuevo vuelo")
    @PostMapping("/new")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vuelo guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al guardar el vuelo"),
            @ApiResponse(responseCode = "409", description = "Conflicto al intentar guardar el vuelo"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al guardar el vuelo")
    })
    public ResponseEntity<?> saveVuelo(@RequestBody Vuelo vuelo) {
        try {
            Vuelo savedVuelo = vueloService.saveVuelo(vuelo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVuelo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar el vuelo: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al guardar el vuelo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el vuelo: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar un vuelo por su ID")
    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún vuelo con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar el vuelo"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al eliminar el vuelo")
    })
    public ResponseEntity<?> deleteVuelo(@PathVariable Long id) {
        try {
            vueloService.deleteVuelo(id);
            return ResponseEntity.ok("Vuelo con ID " + id + " eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún vuelo con ID " + id + ".");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el vuelo: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un vuelo por su ID")
    @PutMapping("/edit/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún vuelo con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el vuelo")
    })
    public ResponseEntity<?> updateVuelo(@PathVariable Long id, @RequestBody VueloDTO vueloDTO) {
        try {
            vueloService.updateVuelo(id, vueloDTO);
            return ResponseEntity.ok("Vuelo con ID " + id + " actualizado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ningún vuelo con el ID " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el vuelo: " + e.getMessage());
        }
    }
}
