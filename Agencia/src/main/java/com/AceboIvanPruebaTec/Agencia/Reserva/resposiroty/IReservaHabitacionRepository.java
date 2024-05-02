package com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.ReservaHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Long> {

    /* Obtiene una lista de reservas de habitaciones que no han sido marcadas como borradas. */
    @Query("SELECT rh FROM ReservaHabitacion rh WHERE rh.borrado = false")
    List<ReservaHabitacion> findNotDeletedReservasHabitacion();

    /* Busca una reserva de habitacion por su ID y asegura que no esté marcado como borrado. */
    ReservaHabitacion findByIdAndBorradoFalse(Long id);
}
