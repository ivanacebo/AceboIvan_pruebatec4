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
@Table(name = "reserva_vuelo")
public class ReservaVuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    private LocalDate fechaReserva;
    private Double precioTotal;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @Where(clause = "borrado = false")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_vuelo")
    @JsonBackReference
    @Where(clause = "borrado = false")
    private Vuelo vuelo;


    private Boolean borrado = false;
}
