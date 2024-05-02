package com.AceboIvanPruebaTec.Agencia.serviceTest;

import com.AceboIvanPruebaTec.Agencia.Reserva.dto.HotelDTO;
import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;
import com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty.IHotelRepository;
import com.AceboIvanPruebaTec.Agencia.Reserva.service.HotelService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @InjectMocks
    private HotelService hotelService;

    @Mock
    private IHotelRepository hotelRepository;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTraerHoteles() {

        Hotel pruebaHotel = new Hotel();
        pruebaHotel.setNombre("Prueba Hotel");
        pruebaHotel.setPais("Prueba Pais");
        pruebaHotel.setCodigo("HO - 325");

        Hotel pruebaHotel2 = new Hotel();
        pruebaHotel2.setNombre("Hotel prueba");
        pruebaHotel2.setPais("Pais prueba");
        pruebaHotel2.setCodigo("HO - 652");

        Hotel pruebaHotel3 = new Hotel();
        pruebaHotel.setNombre("Hotel");
        pruebaHotel.setPais(" Pais");
        pruebaHotel.setCodigo("HO - 965");

        Hotel pruebaHotel4 = new Hotel();
        pruebaHotel2.setNombre("prueba");
        pruebaHotel2.setPais("prueba");
        pruebaHotel2.setCodigo("HO - 362");

        List<Hotel> hoteles = Arrays.asList(pruebaHotel, pruebaHotel2,pruebaHotel4,pruebaHotel3);

        when(hotelRepository.findNotDeletedHotels()).thenReturn(hoteles);

        List<HotelDTO> hotelDTO = hotelService.getHoteles();

        assertEquals(hoteles.size(), hotelDTO.size());
        for (int i = 0; i < hoteles.size(); i++) {
            assertEquals(hoteles.get(i).getNombre(), hotelDTO.get(i).getNombre());
            assertEquals(hoteles.get(i).getPais(), hotelDTO.get(i).getPais());
            assertEquals(hoteles.get(i).getCodigo(), hotelDTO.get(i).getCodigo());
        }
    }

}
