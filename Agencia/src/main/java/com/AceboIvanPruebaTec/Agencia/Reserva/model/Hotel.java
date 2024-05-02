package com.AceboIvanPruebaTec.Agencia.Reserva.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String nombre;
    private String pais;
    private String ciudad;
    private String email;
    private String direccion;
    private String numeroTelefono;

    @OneToMany(mappedBy = "hotel")
    @JsonManagedReference
    @Where(clause = "borrado = false")
    private List<Habitacion> habitaciones;

    private Boolean borrado = false;

}
