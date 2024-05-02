package com.AceboIvanPruebaTec.Agencia.Reserva.dto;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {

    private String codigo;
    private String nombre;
    private String pais;
    private String ciudad;
    private String email;
    private String direccion;
    private String numeroTelefono;
    private List<Habitacion> habitaciones;
}
