package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;

import java.util.List;

public interface IHabitacionService {

    public List<HabitacionDTO> getHabitaciones();

    public Habitacion findHabitacion(Long id);

    public Habitacion saveHabitacion (Habitacion habitacion);

    public void deleteHabitacion (Long id);

    public void updateHabitacion(Long id, HabitacionDTO habitacionDTO);
}
