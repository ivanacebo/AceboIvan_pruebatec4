package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ReservaHabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaHabitacion;

import java.util.List;

public interface IReservaHabitacionService {

    public List<ReservaHabitacionDTO> getReservasHabitaciones();

    public ReservaHabitacion saveReservaHabitacion (ReservaHabitacion reservaHabitacion);

    public ReservaHabitacion deleteReservaHabitacion(Long id);

    public ReservaHabitacion findReservaHabitacion(Long id);

    public void updateReservaHabitacion (Long id, ReservaHabitacionDTO reservaHabitacionDTO);
}
