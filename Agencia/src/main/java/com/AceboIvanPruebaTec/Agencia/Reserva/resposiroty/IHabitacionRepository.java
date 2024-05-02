package com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHabitacionRepository extends JpaRepository<Habitacion, Long> {

    /* Obtiene una lista de habitaciones que no han sido marcadas como borradas. */
    List<Habitacion> findByBorradoFalse();

    /* Busca una habitación por su ID y asegura que no esté marcada como borrada. */
    Habitacion findByIdAndBorradoFalse(Long id);
}
