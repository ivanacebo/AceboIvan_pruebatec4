package com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVueloRepository extends JpaRepository<Vuelo, Long> {

    /* Obtiene una lista de vuelos que no han sido marcados como borrados. */
    List<Vuelo> findByBorradoFalse();

    /* Busca un vuelo por codigo */
    public Vuelo findByCodigo(String codigo);

    /* Busca un vuelo por su ID y asegura que no esté marcado como borrado. */
    Vuelo findByIdAndBorradoFalse(Long id);

}
