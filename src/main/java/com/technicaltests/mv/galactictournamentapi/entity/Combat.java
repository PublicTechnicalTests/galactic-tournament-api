package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a battle in the galactic tournament.
 * <p>
 * This entity records battles between two species and the winner.
 * This entity stores information about battles that occur in the galactic tournament.
 * Each battle has two contenders and a winner determined by their power levels.
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
@Table(
        name = "combats",
        indexes = {
                @Index(name = "idx_winner_id", columnList = "winner_id"),
                @Index(name = "idx_firstContender", columnList = "first_contender_id"),
                @Index(name = "idx_secondContender", columnList = "second_contender_id")
        }
)
public class Combat {

    /**
     * Unique identifier for the battle.
     * -- GETTER --
     * Gets the battle ID.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the battle ID.
     *
     * @return the unique identifier
     * @param combatId the unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "combat_id")
    private Long combatId;

    /**
     * ID of the first contender (species).
     * -- GETTER --
     * Gets the first contender ID.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the first contender ID.
     *
     * @return the first contender species ID
     * @param idContendiente1 the first contender species ID
     */
    @NotNull(message = "First contender ID must not be null")
    @Column(name = "first_contender_id", nullable = false)
    private Long firstContenderId;

    /**
     * ID of the second contender (species).
     * -- GETTER --
     * Gets the second contender ID.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the second contender ID.
     *
     * @return the second contender species ID
     * @param idContendiente2 the second contender species ID
     */
    @NotNull(message = "Second contender ID must not be null")
    @Column(name = "second_contender_id", nullable = false)
    private Long secondContenderId;

    /**
     * ID of the winning species.
     * -- GETTER --
     * Gets the winner ID.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the winner ID.
     *
     * @return the winner species ID
     * @param idGanador the winner species ID
     */
    @NotNull(message = "Winner ID must not be null")
    @Column(name = "winner_id", nullable = false)
    private Long winnerId;

    /**
     * Timestamp of battle creation.
     * -- GETTER --
     * Gets the creation timestamp.
     * <p>
     * <p>
     * -- SETTER --
     * Sets the creation timestamp.
     *
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
        return "Combat{" +
                "combatId=" + combatId +
                ", firstcontenderId=" + firstContenderId +
                ", secondContenderId=" + secondContenderId +
                ", winnerId=" + winnerId +
                ", creationDate=" + creationDate +
                '}';
    }
}

