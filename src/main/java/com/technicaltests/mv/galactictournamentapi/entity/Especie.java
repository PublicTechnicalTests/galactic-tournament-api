package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a Galactic Species.
 *
 * This entity stores information about species participating in galactic tournaments.
 * Each species has unique characteristics including name, power level, and special abilities.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Entity
@Table(name = "especies", uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
public class Especie {

    /**
     * Unique identifier for the species.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especie")
    private Long idEspecie;

    /**
     * Name of the species. Must be unique and not null.
     */
    @NotBlank(message = "Species name must not be blank")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Power level of the species. Must be positive and not null.
     */
    @NotNull(message = "Species power must not be null")
    @Positive(message = "Species power must be positive")
    @Column(name = "poder", nullable = false)
    private Integer poder;

    /**
     * Special ability of the species. Must not be blank.
     */
    @NotBlank(message = "Species ability must not be blank")
    @Column(name = "habilidad", nullable = false, length = 255)
    private String habilidad;

    /**
     * Creation timestamp. Automatically set when the entity is persisted.
     */
    @CreationTimestamp
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

    /**
     * Gets the species ID.
     *
     * @return the unique identifier
     */
    public Long getIdEspecie() {
        return idEspecie;
    }

    /**
     * Sets the species ID.
     *
     * @param idEspecie the unique identifier
     */
    public void setIdEspecie(Long idEspecie) {
        this.idEspecie = idEspecie;
    }

    /**
     * Gets the species name.
     *
     * @return the name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the species name.
     *
     * @param nombre the name to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the power level.
     *
     * @return the power level
     */
    public Integer getPoder() {
        return poder;
    }

    /**
     * Sets the power level.
     *
     * @param poder the power level to set
     */
    public void setPoder(Integer poder) {
        this.poder = poder;
    }

    /**
     * Gets the special ability.
     *
     * @return the ability description
     */
    public String getHabilidad() {
        return habilidad;
    }

    /**
     * Sets the special ability.
     *
     * @param habilidad the ability description to set
     */
    public void setHabilidad(String habilidad) {
        this.habilidad = habilidad;
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
        return "Especie{" +
                "idEspecie=" + idEspecie +
                ", nombre='" + nombre + '\'' +
                ", poder=" + poder +
                ", habilidad='" + habilidad + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}

