package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entity representing a battle in the galactic tournament.
 *
 * This entity records battles between two species and the winner.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Entity
@Table(
        name = "combates",
        indexes = {
                @Index(name = "idx_id_ganador", columnList = "id_ganador"),
                @Index(name = "idx_contendiente1", columnList = "id_contendiente_1"),
                @Index(name = "idx_contendiente2", columnList = "id_contendiente_2")
        }
)
public class Combate {

    /**
     * Unique identifier for the battle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_combate")
    private Long idCombate;

    /**
     * ID of the first contender (species).
     */
    @NotNull(message = "First contender ID must not be null")
    @Column(name = "id_contendiente_1", nullable = false)
    private Long idContendiente1;

    /**
     * ID of the second contender (species).
     */
    @NotNull(message = "Second contender ID must not be null")
    @Column(name = "id_contendiente_2", nullable = false)
    private Long idContendiente2;

    /**
     * ID of the winning species.
     */
    @NotNull(message = "Winner ID must not be null")
    @Column(name = "id_ganador", nullable = false)
    private Long idGanador;

    /**
     * Timestamp of battle creation.
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Constructors

    /**
     * Default constructor for JPA.
     */
    public Combate() {
    }

    /**
     * Constructor with contenders and winner.
     *
     * @param idContendiente1 first contender species ID
     * @param idContendiente2 second contender species ID
     * @param idGanador winner species ID
     */
    public Combate(Long idContendiente1, Long idContendiente2, Long idGanador) {
        this.idContendiente1 = idContendiente1;
        this.idContendiente2 = idContendiente2;
        this.idGanador = idGanador;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Lifecycle callbacks

    /**
     * Set creation date before persisting.
     */
    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }

    // Getters and Setters

    /**
     * Gets the battle ID.
     *
     * @return the unique identifier
     */
    public Long getIdCombate() {
        return idCombate;
    }

    /**
     * Sets the battle ID.
     *
     * @param idCombate the unique identifier
     */
    public void setIdCombate(Long idCombate) {
        this.idCombate = idCombate;
    }

    /**
     * Gets the first contender ID.
     *
     * @return the first contender species ID
     */
    public Long getIdContendiente1() {
        return idContendiente1;
    }

    /**
     * Sets the first contender ID.
     *
     * @param idContendiente1 the first contender species ID
     */
    public void setIdContendiente1(Long idContendiente1) {
        this.idContendiente1 = idContendiente1;
    }

    /**
     * Gets the second contender ID.
     *
     * @return the second contender species ID
     */
    public Long getIdContendiente2() {
        return idContendiente2;
    }

    /**
     * Sets the second contender ID.
     *
     * @param idContendiente2 the second contender species ID
     */
    public void setIdContendiente2(Long idContendiente2) {
        this.idContendiente2 = idContendiente2;
    }

    /**
     * Gets the winner ID.
     *
     * @return the winner species ID
     */
    public Long getIdGanador() {
        return idGanador;
    }

    /**
     * Sets the winner ID.
     *
     * @param idGanador the winner species ID
     */
    public void setIdGanador(Long idGanador) {
        this.idGanador = idGanador;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return the creation date and time
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param fechaCreacion the creation date and time
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Combate{" +
                "idCombate=" + idCombate +
                ", idContendiente1=" + idContendiente1 +
                ", idContendiente2=" + idContendiente2 +
                ", idGanador=" + idGanador +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}

