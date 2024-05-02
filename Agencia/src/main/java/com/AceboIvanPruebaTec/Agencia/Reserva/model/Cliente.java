package com.AceboIvanPruebaTec.Agencia.Reserva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    @Where(clause = "borrado = false")
    private List<ReservaHabitacion> reservasHabitacion = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    @Where(clause = "borrado = false")
    private List<ReservaVuelo> reservasVuelo = new ArrayList<>();

    private Boolean borrado = false;
}
