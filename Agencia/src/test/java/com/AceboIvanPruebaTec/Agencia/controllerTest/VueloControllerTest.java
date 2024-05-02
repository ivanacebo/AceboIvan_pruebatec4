package com.AceboIvanPruebaTec.Agencia.controllerTest;

import com.AceboIvanPruebaTec.Agencia.Reserva.controller.HotelController;
import com.AceboIvanPruebaTec.Agencia.Reserva.controller.VueloController;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IHotelService;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IVueloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class VueloControllerTest {

    @InjectMocks
    private VueloController vueloController;

    @Mock
    private IVueloService vueloService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarVuelo() throws Exception {
        //given
        Vuelo vuelo = new Vuelo();
        vuelo.setTipoAsiento(25);
        vuelo.setCantidadAsientos(25);
        vuelo.setOrigen("Paris");
        vuelo.setDestino("Madrid");
        vuelo.setEsDirecto("escala");

        given(vueloService.saveVuelo(any(Vuelo.class))).willReturn(vuelo);

        // when
        vueloController.saveVuelo(vuelo);

        // then
        verify(vueloService).saveVuelo(vuelo);
    }

    @Test
    public void buscarVuelos() {
        // given
        vueloController.getVuelos();

        // then
        verify(vueloService).getVuelos();
    }

}
