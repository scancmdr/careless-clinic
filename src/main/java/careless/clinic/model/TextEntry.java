package careless.clinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a text entry in the system.
 * <p>
 * Stores user-submitted text content with timestamps.
 *
 * @author jay
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "text_entries")
public class TextEntry {
    /** Primary key identifier */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Entry title (max 1255 characters) */
    @Column(length = 1255)
    private String title;

    /** Entry content (max 12032 characters) */
    @Column(length = 12032)
    private String content;

    /** Timestamp when entry was created */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Timestamp when entry was last updated */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Indicates if content is a valid JWT token (not persisted) */
    @Transient
    private boolean jwtVerified;
}
