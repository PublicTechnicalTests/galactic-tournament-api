package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
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
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "especies", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Specie {

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
     * @param specieId the unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specie_id")
    private Long specieId;

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
     * @param name the name to set
     */
    @NotBlank(message = "Species name must not be blank")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

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
     * @param power the power level to set
     */
    @NotNull(message = "Species power must not be null")
    @Positive(message = "Species power must be positive")
    @Column(name = "power", nullable = false)
    private Integer power;

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
     * @param ability the ability description to set
     */
    @NotBlank(message = "Species ability must not be blank")
    @Column(name = "ability", nullable = false, length = 255)
    private String ability;

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
     * @param creationDate the creation date and time
     */
    @CurrentTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    // Lifecycle callbacks

    /**
     * Set creation date before persisting.
     */
    @PrePersist
    protected void onCreate() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now();
        }
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "Specie{" +
                "specieId=" + specieId +
                ", name='" + name + '\'' +
                ", power=" + power +
                ", ability='" + ability + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
