package com.AceboIvanPruebaTec.Agencia.Reserva.dto;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaHabitacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionDTO {

    private Integer numeroHabitacion;
    private Integer capacidad;
    private String tipoHabitacion;
    private String estado;
    private Double precioDia;
    private String descripcion;
    private LocalDate disponibleDesde;
    private LocalDate disponibleHasta;
    private Hotel hotel;
    private List<ReservaHabitacion> listaReservas;
}
