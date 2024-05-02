package com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaVueloRepository extends JpaRepository<ReservaVuelo, Long> {

    /* Busca una reserva de vuelo por su ID y asegura que no esté marcado como borrado. */
    ReservaVuelo findByIdAndBorradoFalse(Long id);

    /* Obtiene una lista de reservas de habitaciones que no han sido marcadas como borradas. */
    @Query("SELECT rh FROM ReservaVuelo rh WHERE rh.borrado = false")
    List<ReservaVuelo> findNotDeletedReservasvuelo();
}
