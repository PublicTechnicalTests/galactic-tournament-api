package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a Battle between two species.
 *
 * This entity stores information about battles that occur in the galactic tournament.
 * Each battle has two contenders and a winner determined by their power levels.
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
                @Index(name = "idx_fecha_creacion", columnList = "fecha_creacion")
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
     * ID of the first contender species.
     */
    @NotNull(message = "First contender must not be null")
    @Column(name = "id_contendiente_1", nullable = false)
    private Long idContendiente1;

    /**
     * ID of the second contender species.
     */
    @NotNull(message = "Second contender must not be null")
    @Column(name = "id_contendiente_2", nullable = false)
    private Long idContendiente2;

    /**
     * ID of the winner species.
     */
    @NotNull(message = "Winner must not be null")
    @Column(name = "id_ganador", nullable = false)
    private Long idGanador;

    /**
     * Creation timestamp. Automatically set when the battle is created.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Constructors

    /**
     * Default constructor for JPA.
     */
    public Combate() {
    }

    /**
     * Constructor with all required fields.
     *
     * @param idContendiente1 ID of first contender
     * @param idContendiente2 ID of second contender
     * @param idGanador       ID of winner
     */
    public Combate(Long idContendiente1, Long idContendiente2, Long idGanador) {
        this.idContendiente1 = idContendiente1;
        this.idContendiente2 = idContendiente2;
        this.idGanador = idGanador;
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
     * @return the contender ID
     */
    public Long getIdContendiente1() {
        return idContendiente1;
    }

    /**
     * Sets the first contender ID.
     *
     * @param idContendiente1 the contender ID to set
     */
    public void setIdContendiente1(Long idContendiente1) {
        this.idContendiente1 = idContendiente1;
    }

    /**
     * Gets the second contender ID.
     *
     * @return the contender ID
     */
    public Long getIdContendiente2() {
        return idContendiente2;
    }

    /**
     * Sets the second contender ID.
     *
     * @param idContendiente2 the contender ID to set
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
     * @param idGanador the winner species ID to set
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

