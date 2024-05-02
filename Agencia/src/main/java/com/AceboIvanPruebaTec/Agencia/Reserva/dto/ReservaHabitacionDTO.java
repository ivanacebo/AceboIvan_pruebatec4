package com.AceboIvanPruebaTec.Agencia.Reserva.dto;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Cliente;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaHabitacionDTO {

    private String estado;
    private LocalDate fechaReserva;
    private Double precioTotal;
    private Cliente cliente;
    private Habitacion habitacion;

}
