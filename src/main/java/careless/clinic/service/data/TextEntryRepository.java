package careless.clinic.service.data;

import careless.clinic.model.TextEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TextEntryRepository extends JpaRepository<TextEntry, Long> {

    List<TextEntry> findByTitleContainingIgnoreCase(String title);

    List<TextEntry> findByContentContainingIgnoreCase(String content);

    List<TextEntry> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM TextEntry t WHERE t.title LIKE %:searchTerm% OR t.content LIKE %:searchTerm%")
    List<TextEntry> searchByTitleOrContent(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM text_entries ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<TextEntry> findRecentEntries(@Param("limit") int limit);
}
