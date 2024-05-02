package com.AceboIvanPruebaTec.Agencia.controllerTest;

import com.AceboIvanPruebaTec.Agencia.Reserva.controller.HabitacionController;
import com.AceboIvanPruebaTec.Agencia.Reserva.controller.VueloController;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IHabitacionService;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.IVueloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class HabitacionControllerTest {
    @InjectMocks
    private HabitacionController habitacionController;

    @Mock
    private IHabitacionService habitacionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarHabitacion() throws Exception {
        //given
        Habitacion habitacion = new Habitacion();
        habitacion.setNumeroHabitacion(60);
        habitacion.setCapacidad(4);
        habitacion.setTipoHabitacion("suit");
        habitacion.setEstado("pagada");
        habitacion.setPrecioDia(35.45);

        given(habitacionService.saveHabitacion(any(Habitacion.class))).willReturn(habitacion);

        // when
        habitacionService.saveHabitacion(habitacion);

        // then
        verify(habitacionService).saveHabitacion(habitacion);
    }

    @Test
    public void buscarHabitaciones() {
        // given
        habitacionController.getHabitaciones();

        // then
        verify(habitacionService).getHabitaciones();
    }
}
