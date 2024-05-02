package com.AceboIvanPruebaTec.Agencia.Reserva.dto;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Cliente;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaVueloDTO {

    private String estado;
    private LocalDate fechaReserva;
    private Double precioTotal;
    private Vuelo vuelo;
    private Cliente cliente;
}
