package com.AceboIvanPruebaTec.Agencia.Reserva.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "vuelo")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private Integer tipoAsiento;
    private Integer cantidadAsientos;
    private String origen;
    private String destino;
    private LocalDate fechaPartida;
    private LocalDate fechaLlegada;
    private Double millas;
    private String esDirecto;
    private String aerolinea;
    private Double precio;

    @OneToMany(mappedBy = "vuelo")
    @Where(clause = "borrado = false")
    private List<ReservaVuelo> listaReservas;

    private Boolean borrado = false;
}
