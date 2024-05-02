package com.AceboIvanPruebaTec.Agencia.Reserva.service;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.VueloDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;

import java.time.LocalDate;
import java.util.List;

public interface IVueloService {

    public List<VueloDTO> getVuelos();

    public Vuelo saveVuelo (Vuelo vuelo);

    public void deleteVuelo (Long id);

    public Vuelo findVuelo (Long id);

    public void updateVuelo(Long id, VueloDTO vueloDTO);

    public List<Vuelo> getVuelosFiltro(String origen, String destino, LocalDate fechaSalida, LocalDate fechaLlegada);
}
