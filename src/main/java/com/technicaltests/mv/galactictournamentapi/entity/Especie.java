package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a Galactic Species.
 * <p>
 * This entity stores information about species participating in galactic tournaments.
 * Each species has unique characteristics including name, power level, and special abilities.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Setter
@Getter
@Entity
@Table(name = "especies", uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
public class Especie {

    /**
     * Unique identifier for the species.
     * -- GETTER --
     * Gets the species ID.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the species ID.
     *
     * @return the unique identifier
     * @param idEspecie the unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especie")
    private Long idEspecie;

    /**
     * Name of the species. Must be unique and not null.
     * -- GETTER --
     * Gets the species name.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the species name.
     *
     * @return the name
     * @param nombre the name to set
     */
    @NotBlank(message = "Species name must not be blank")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Power level of the species. Must be positive and not null.
     * -- GETTER --
     * Gets the power level.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the power level.
     *
     * @return the power level
     * @param poder the power level to set
     */
    @NotNull(message = "Species power must not be null")
    @Positive(message = "Species power must be positive")
    @Column(name = "poder", nullable = false)
    private Integer poder;

    /**
     * Special ability of the species. Must not be blank.
     * -- GETTER --
     * Gets the special ability.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the special ability.
     *
     * @return the ability description
     * @param habilidad the ability description to set
     */
    @NotBlank(message = "Species ability must not be blank")
    @Column(name = "habilidad", nullable = false, length = 255)
    private String habilidad;

    /**
     * Creation timestamp. Automatically set when the entity is persisted.
     * -- GETTER --
     * Gets the creation timestamp.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the creation timestamp.
     *
     * @return the creation date and time
     * @param fechaCreacion the creation date and time
     */
    @CurrentTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Constructors

    /**
     * Default constructor for JPA.
     */
    public Especie() {
    }

    /**
     * Constructor with all fields except ID and creation date.
     *
     * @param nombre    the name of the species
     * @param poder     the power level of the species
     * @param habilidad the special ability of the species
     */
    public Especie(String nombre, Integer poder, String habilidad) {
        this.nombre = nombre;
        this.poder = poder;
        this.habilidad = habilidad;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "Especie{" +
                "idEspecie=" + idEspecie +
                ", nombre='" + nombre + '\'' +
                ", poder=" + poder +
                ", habilidad='" + habilidad + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
