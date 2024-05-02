package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.ReservaVueloDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaVuelo;

import java.util.List;

public interface IReservaVueloService {

    public List<ReservaVueloDTO> getReservaVuelos();

    public ReservaVuelo saveReservaVuelo(ReservaVuelo reservaVuelo);

    public ReservaVuelo deleteReservaVuelo(Long id);

    public ReservaVuelo findReservaVuelo(Long id);
    public void updateReservaVuelo (Long id, ReservaVueloDTO reservaVueloDTO);

}
