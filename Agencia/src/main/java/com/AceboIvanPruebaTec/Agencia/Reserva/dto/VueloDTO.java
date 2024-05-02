package com.AceboIvanPruebaTec.Agencia.Reserva.dto;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaVuelo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VueloDTO {

    private String codigo;
    private Integer tipoAsiento;
    private Integer cantidadAsientos;
    private String origen;
    private String destino;
    private LocalDate fechaPartida;
    private LocalDate fechaLlegada;
    private Double millas;
    private String esDirecto;
    private String aerolinea;
    private List<ReservaVuelo> listaReservas;
}
