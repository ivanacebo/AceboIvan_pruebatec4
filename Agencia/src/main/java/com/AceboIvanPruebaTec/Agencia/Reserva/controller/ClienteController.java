package com.AceboIvanPruebaTec.Agencia.Reserva.controller;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ClienteDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Cliente;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IClienteService;
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
@RequestMapping("/agency/clients")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Operation(summary = "Obtener todos los clientes disponibles")
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay clientes disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener los clientes")
    })
    public ResponseEntity<?> getClientes() {
        try {
            List<ClienteDTO> clientes = clienteService.getClientes();

            if (clientes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay clientes disponibles.");
            } else {
                return ResponseEntity.ok(clientes);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron clientes disponibles.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los clientes: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar un cliente por su ID")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún cliente con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al buscar el cliente")
    })
    public ResponseEntity<?> findClienteById(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.findCliente(id);
            return ResponseEntity.ok(cliente);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el cliente: " + e.getMessage());
        }
    }

    @Operation(summary = "Guardar un nuevo cliente")
    @PostMapping("/new")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al guardar el cliente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al guardar el cliente")
    })
    public ResponseEntity<?> saveCliente(@RequestBody Cliente cliente) {
        try {
            Cliente savedCliente = clienteService.saveCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar el cliente: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el cliente: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar un cliente por su ID")
    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún cliente con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar el cliente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al eliminar el cliente")
    })
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.ok("Cliente con ID " + id + " eliminado correctamente.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el cliente: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un cliente por su ID")
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún cliente con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el cliente")
    })
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        try {
            clienteService.updateCliente(id, clienteDTO);
            return ResponseEntity.ok("Cliente con ID " + id + " actualizado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el cliente: " + e.getMessage());
        }
    }
}

