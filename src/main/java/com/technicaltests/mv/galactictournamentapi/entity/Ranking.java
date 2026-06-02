package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a species ranking in the galactic tournament.
 *
 * This entity tracks the number of victories each species has achieved.
 * It is updated whenever a battle is completed.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Entity
@Table(
        name = "rankings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id_especie", name = "uk_ranking_especie")
        },
        indexes = {
                @Index(name = "idx_victorias", columnList = "victorias DESC"),
                @Index(name = "idx_id_especie", columnList = "id_especie")
        }
)
public class Ranking {

    /**
     * Unique identifier for the ranking record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ranking")
    private Long idRanking;

    /**
     * ID of the species this ranking belongs to.
     * Must be unique - one ranking per species.
     */
    @NotNull(message = "Species ID must not be null")
    @Column(name = "id_especie", nullable = false, unique = true)
    private Long idEspecie;

    /**
     * Number of victories for this species.
     * Starts at 0 when ranking is created.
     * Incremented each time the species wins a battle.
     */
    @NotNull(message = "Victories must not be null")
    @Column(name = "victorias", nullable = false)
    private Long victorias = 0L;

    // Constructors

    /**
     * Default constructor for JPA.
     */
    public Ranking() {
    }

    /**
     * Constructor with species ID.
     *
     * @param idEspecie the species ID
     */
    public Ranking(Long idEspecie) {
        this.idEspecie = idEspecie;
        this.victorias = 0L;
    }

    /**
     * Constructor with all fields.
     *
     * @param idEspecie the species ID
     * @param victorias the number of victories
     */
    public Ranking(Long idEspecie, Long victorias) {
        this.idEspecie = idEspecie;
        this.victorias = victorias;
    }

    // Getters and Setters

    /**
     * Gets the ranking ID.
     *
     * @return the unique identifier
     */
    public Long getIdRanking() {
        return idRanking;
    }

    /**
     * Sets the ranking ID.
     *
     * @param idRanking the unique identifier
     */
    public void setIdRanking(Long idRanking) {
        this.idRanking = idRanking;
    }

    /**
     * Gets the species ID.
     *
     * @return the species ID
     */
    public Long getIdEspecie() {
        return idEspecie;
    }

    /**
     * Sets the species ID.
     *
     * @param idEspecie the species ID to set
     */
    public void setIdEspecie(Long idEspecie) {
        this.idEspecie = idEspecie;
    }

    /**
     * Gets the number of victories.
     *
     * @return the victory count
     */
    public Long getVictorias() {
        return victorias;
    }

    /**
     * Sets the number of victories.
     *
     * @param victorias the victory count to set
     */
    public void setVictorias(Long victorias) {
        this.victorias = victorias;
    }

    /**
     * Increments the victory count by 1.
     * Useful for updating ranking after a battle win.
     */
    public void addVictory() {
        this.victorias = (this.victorias != null ? this.victorias : 0L) + 1;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "idRanking=" + idRanking +
                ", idEspecie=" + idEspecie +
                ", victorias=" + victorias +
                '}';
    }
}

