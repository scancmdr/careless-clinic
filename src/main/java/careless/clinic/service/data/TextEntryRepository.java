package careless.clinic.service.data;

import careless.clinic.model.TextEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA repository for {@link TextEntry} entities.
 * <p>
 * Provides CRUD operations and custom query methods.
 *
 * @author jay
 * @see TextEntry
 * @see JpaRepository
 */
@Repository
public interface TextEntryRepository extends JpaRepository<TextEntry, Long> {

    /**
     * Finds entries with title containing search term (case-insensitive).
     *
     * @param title search term for title
     * @return matching text entries
     */
    List<TextEntry> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds entries with content containing search term (case-insensitive).
     *
     * @param content search term for content
     * @return matching text entries
     */
    List<TextEntry> findByContentContainingIgnoreCase(String content);

    /**
     * Finds entries created within time range.
     *
     * @param start start of time range
     * @param end end of time range
     * @return entries created in range
     */
    List<TextEntry> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Searches entries by title or content using JPQL query.
     *
     * @param searchTerm search term
     * @return matching text entries
     */
    @Query("SELECT t FROM TextEntry t WHERE t.title LIKE %:searchTerm% OR t.content LIKE %:searchTerm%")
    List<TextEntry> searchByTitleOrContent(@Param("searchTerm") String searchTerm);

    /**
     * Retrieves most recent entries using native SQL.
     *
     * @param limit maximum number of entries to return
     * @return recent text entries
     */
    @Query(value = "SELECT * FROM text_entries ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<TextEntry> findRecentEntries(@Param("limit") int limit);
}
