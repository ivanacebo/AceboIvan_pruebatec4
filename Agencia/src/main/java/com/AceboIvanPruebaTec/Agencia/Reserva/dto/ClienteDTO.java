package com.AceboIvanPruebaTec.Agencia.Reserva.dto;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaHabitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaVuelo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private List<ReservaHabitacion> reservasHabitacion;
    private List<ReservaVuelo> reservasVuelo;
}
