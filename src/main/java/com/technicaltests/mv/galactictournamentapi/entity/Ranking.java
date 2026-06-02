package com.technicaltests.mv.galactictournamentapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entity representing a species ranking in the galactic tournament.
 * <p>
 * This entity tracks the number of victories each species has achieved.
 * It is updated whenever a battle is completed.
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
        name = "rankings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "spacie_id", name = "uk_ranking_specie")
        },
        indexes = {
                @Index(name = "idx_victories", columnList = "victories DESC"),
                @Index(name = "idx_specie_id", columnList = "specie_id")
        }
)
public class Ranking {

    /**
     * Unique identifier for the ranking record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_id")
    private Long rankingId;

    /**
     * ID of the species this ranking belongs to.
     * Must be unique - one ranking per species.
     */
    @NotNull(message = "Species ID must not be null")
    @Column(name = "specie_id", nullable = false, unique = true)
    private Long specieId;

    /**
     * Number of victories for this species.
     * Starts at 0 when ranking is created.
     * Incremented each time the species wins a battle.
     */
    @NotNull(message = "Victories must not be null")
    @Column(name = "victorias", nullable = false)
    @Builder.Default
    private Long victories = 0L;

    public void addVictory() {
        this.victories++;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "idRanking=" + rankingId +
                ", specieId=" + specieId +
                ", victorias=" + victories +
                '}';
    }
}

