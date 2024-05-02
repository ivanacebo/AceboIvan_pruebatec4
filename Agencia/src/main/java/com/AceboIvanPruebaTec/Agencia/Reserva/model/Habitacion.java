package com.AceboIvanPruebaTec.Agencia.Reserva.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "habitacion")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numeroHabitacion;
    private Integer capacidad;
    private String tipoHabitacion;
    private String estado;
    private Double precioDia;
    private String descripcion;
    private LocalDate disponibleDesde;
    private LocalDate disponibleHasta;

    @ManyToOne
    @JoinColumn(name = "id_hotel")
    @JsonBackReference
    @Where(clause = "borrado = false")
    private Hotel hotel;

    @OneToMany(mappedBy = "habitacion")
    @Where(clause = "borrado = false")
    private List<ReservaHabitacion> listaReservas;

    private Boolean borrado = false;
}
