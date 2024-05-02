package com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHotelRepository extends JpaRepository<Hotel, Long> {

    /* Busca un hotel por codigo */
    public Hotel findByCodigo(String codigo);

    /* Obtiene una lista de hoteles que no han sido marcados como borrados. */
    @Query("SELECT h FROM Hotel h WHERE h.borrado = false")
    List<Hotel> findNotDeletedHotels();

    /* Busca un hotel por su ID y asegura que no esté marcado como borrado. */
    Hotel findByIdAndBorradoFalse(Long id);
}
