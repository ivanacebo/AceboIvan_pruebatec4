package com.AceboIvanPruebaTec.Agencia.Reserva.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "reserva_habitacion")
public class ReservaHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    private LocalDate fechaReserva;
    private Double precioTotal;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    @JsonBackReference
    @Where(clause = "borrado = false")
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @Where(clause = "borrado = false")
    private Cliente cliente;
    private Boolean borrado = false;

}
