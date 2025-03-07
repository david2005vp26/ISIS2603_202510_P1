package co.edu.uniandes.dse.parcial1.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ConciertoEntity extends BaseEntity {

    private String nombre;
    private Long presupuesto;
    private LocalDateTime fechaConcierto;
    private int capacidadAforo;

    @PodamExclude
    @ManyToOne
    private EstadioEntity estadio;

}
