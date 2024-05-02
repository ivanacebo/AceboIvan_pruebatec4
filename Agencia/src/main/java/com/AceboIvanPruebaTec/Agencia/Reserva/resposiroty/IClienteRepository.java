package com.AceboIvanPruebaTec.Agencia.Reserva.resposiroty;

import com.AceboIvanPruebaTec.Agencia.Reserva.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

    /* Busca un cliente por su DNI. */
    Cliente findByDni(String dni);

    /* Obtiene una lista de clientes que no han sido marcados como borrados. */
    @Query("SELECT c FROM Cliente c WHERE c.borrado = false")
    List<Cliente> findNotDeletedClientes();

    /* Busca un cliente por su ID y asegura que no esté marcado como borrado. */
    Cliente findByIdAndBorradoFalse(Long id);
}
