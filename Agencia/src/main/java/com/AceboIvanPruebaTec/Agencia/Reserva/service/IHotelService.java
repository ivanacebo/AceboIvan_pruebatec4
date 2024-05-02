package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HabitacionDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HotelDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;

import java.time.LocalDate;
import java.util.List;

public interface IHotelService {

    //Hoteles

    public Hotel saveHotel(Hotel hotel);

    public List<HotelDTO> getHoteles();

    public void deleteHotel(Long id);

    public Hotel findHotel(Long id);

    public void updateHotel(Long id, HotelDTO hotelDTO);

    public List<Habitacion> getHabitacionesFiltro(LocalDate disponibleDesde, LocalDate disponibleHasta, String pais);

}
