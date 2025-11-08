package careless.clinic.service.data;

import careless.clinic.model.TextEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataService {

    private final TextEntryRepository textEntryRepository;
    private final JdbcTemplate jdbcTemplate;

    // ==================== JPA Repository Methods ====================

    @Transactional
    public TextEntry saveEntry(TextEntry entry) {
        log.debug("Saving TextEntry via JPA: {}", entry);
        return textEntryRepository.save(entry);
    }

    public Optional<TextEntry> findById(Long id) {
        log.debug("Finding TextEntry by id via JPA: {}", id);
        return textEntryRepository.findById(id);
    }

    public List<TextEntry> findAll() {
        log.debug("Finding all TextEntries via JPA");
        return textEntryRepository.findAll();
    }

    public List<TextEntry> searchByTitleOrContent(String searchTerm) {
        log.debug("Searching TextEntries by title or content via JPA: {}", searchTerm);
        return textEntryRepository.searchByTitleOrContent(searchTerm);
    }

    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting TextEntry by id via JPA: {}", id);
        textEntryRepository.deleteById(id);
    }

    // ==================== Direct JDBC Methods ====================

    public TextEntry saveEntryViaJdbc(TextEntry entry) {
        log.debug("Saving TextEntry via JDBC: {}", entry);

        if (entry.getId() == null) {
            // Insert new entry
            String sql = "INSERT INTO text_entries (title, content, created_at, updated_at) VALUES (?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                entry.getTitle(),
                entry.getContent(),
                LocalDateTime.now(),
                LocalDateTime.now()
            );
            entry.setId(id);
        } else {
            // Update existing entry
            String sql = "UPDATE text_entries SET title = ?, content = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                entry.getTitle(),
                entry.getContent(),
                LocalDateTime.now(),
                entry.getId()
            );
        }

        return entry;
    }

    public Optional<TextEntry> findByIdViaJdbc(Long id) {
        log.debug("Finding TextEntry by id via JDBC: {}", id);

        String sql = "SELECT id, title, content, created_at, updated_at FROM text_entries WHERE id = ?";

        List<TextEntry> results = jdbcTemplate.query(sql, new TextEntryRowMapper(), id);

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<TextEntry> findAllViaJdbc() {
        log.debug("Finding all TextEntries via JDBC");

        String sql = "SELECT id, title, content, created_at, updated_at FROM text_entries ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, new TextEntryRowMapper());
    }

    public List<TextEntry> searchByTitleOrContentViaJdbc(String searchTerm) {
        log.debug("Searching TextEntries by title or content via JDBC: {}", searchTerm);

        String sql = "SELECT id, title, content, created_at, updated_at FROM text_entries " +
                     "WHERE title ILIKE ? OR content ILIKE ?";

        String likePattern = "%" + searchTerm + "%";

        return jdbcTemplate.query(sql, new TextEntryRowMapper(), likePattern, likePattern);
    }

    public int deleteByIdViaJdbc(Long id) {
        log.debug("Deleting TextEntry by id via JDBC: {}", id);

        String sql = "DELETE FROM text_entries WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    // ==================== Raw SQL Execution Methods ====================
    // WARNING: These methods are intentionally vulnerable for educational purposes
    // They accept unsanitized SQL and should NEVER be used in production

    /**
     * Execute raw SQL query returning TextEntry objects - USE WITH CAUTION
     * This method is intentionally vulnerable for educational purposes
     */
    public List<TextEntry> executeRawQuery(String sqlQuery) {
        log.warn("Executing raw SQL query: {}", sqlQuery);

        return jdbcTemplate.query(sqlQuery, new TextEntryRowMapper());
    }

    /**
     * Execute raw SQL query returning a list of maps - USE WITH CAUTION
     * Each map represents a row with column names as keys
     */
    public List<java.util.Map<String, Object>> executeRawQueryForMaps(String sqlQuery) {
        log.warn("Executing raw SQL query for maps: {}", sqlQuery);

        return jdbcTemplate.queryForList(sqlQuery);
    }

    /**
     * Execute raw SQL query returning a single map - USE WITH CAUTION
     * Returns null if no results found
     */
    public java.util.Map<String, Object> executeRawQueryForMap(String sqlQuery) {
        log.warn("Executing raw SQL query for single map: {}", sqlQuery);

        try {
            return jdbcTemplate.queryForMap(sqlQuery);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            log.debug("Query returned no results");
            return null;
        }
    }

    /**
     * Execute raw SQL query returning a single value - USE WITH CAUTION
     * Useful for COUNT, SUM, MAX, etc.
     */
    public <T> T executeRawQueryForObject(String sqlQuery, Class<T> requiredType) {
        log.warn("Executing raw SQL query for object: {}", sqlQuery);

        try {
            return jdbcTemplate.queryForObject(sqlQuery, requiredType);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            log.debug("Query returned no results");
            return null;
        }
    }

    /**
     * Execute raw SQL update/insert/delete statement - USE WITH CAUTION
     * Returns the number of rows affected
     */
    public int executeRawUpdate(String sqlStatement) {
        log.warn("Executing raw SQL update: {}", sqlStatement);

        return jdbcTemplate.update(sqlStatement);
    }

    /**
     * Execute raw SQL statement (any type) - USE WITH CAUTION
     * This is the most dangerous method - executes any SQL without validation
     * Returns true if the statement returns a result set, false otherwise
     */
    public boolean executeRawStatement(String sqlStatement) {
        log.warn("Executing raw SQL statement: {}", sqlStatement);

        return jdbcTemplate.execute((java.sql.Connection conn) -> {
            try (java.sql.Statement stmt = conn.createStatement()) {
                return stmt.execute(sqlStatement);
            }
        });
    }

    /**
     * Execute multiple raw SQL statements separated by semicolons - USE WITH CAUTION
     * Returns the number of statements executed
     */
    public int executeBatchRawStatements(String sqlStatements) {
        log.warn("Executing batch raw SQL statements: {}", sqlStatements);

        String[] statements = sqlStatements.split(";");
        int count = 0;

        for (String statement : statements) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                jdbcTemplate.execute(trimmed);
                count++;
            }
        }

        return count;
    }

    // ==================== Row Mapper ====================

    private static class TextEntryRowMapper implements RowMapper<TextEntry> {
        @Override
        public TextEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return TextEntry.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .content(rs.getString("content"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                    .build();
        }
    }
}
