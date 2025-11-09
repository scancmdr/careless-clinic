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

/**
 * Service providing both JPA and JDBC access to {@link TextEntry} data.
 * <p>
 * WARNING: Contains intentionally vulnerable raw SQL methods for educational purposes.
 *
 * @author jay
 * @see TextEntry
 * @see TextEntryRepository
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataService {

    private final TextEntryRepository textEntryRepository;
    private final JdbcTemplate jdbcTemplate;

    // ==================== JPA Repository Methods ====================

    /**
     * Saves entry using JPA repository.
     *
     * @param entry text entry to save
     * @return saved text entry
     */
    @Transactional
    public TextEntry saveEntry(TextEntry entry) {
        log.debug("Saving TextEntry via JPA: {}", entry);
        return textEntryRepository.save(entry);
    }

    /**
     * Finds entry by ID using JPA repository.
     *
     * @param id entry ID
     * @return optional containing entry if found
     */
    public Optional<TextEntry> findById(Long id) {
        log.debug("Finding TextEntry by id via JPA: {}", id);
        return textEntryRepository.findById(id);
    }

    /**
     * Retrieves all entries using JPA repository.
     *
     * @return list of all entries
     */
    public List<TextEntry> findAll() {
        log.debug("Finding all TextEntries via JPA");
        return textEntryRepository.findAll();
    }

    /**
     * Searches entries by title or content using JPA repository.
     *
     * @param searchTerm search term
     * @return matching entries
     */
    public List<TextEntry> searchByTitleOrContent(String searchTerm) {
        log.debug("Searching TextEntries by title or content via JPA: {}", searchTerm);
        return textEntryRepository.searchByTitleOrContent(searchTerm);
    }

    /**
     * Deletes entry by ID using JPA repository.
     *
     * @param id entry ID to delete
     */
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting TextEntry by id via JPA: {}", id);
        textEntryRepository.deleteById(id);
    }

    // ==================== Direct JDBC Methods ====================

    /**
     * Saves entry using JDBC template with prepared statements.
     *
     * @param entry text entry to save
     * @return saved text entry with ID
     */
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

    /**
     * Finds entry by ID using JDBC template.
     *
     * @param id entry ID
     * @return optional containing entry if found
     */
    public Optional<TextEntry> findByIdViaJdbc(Long id) {
        log.debug("Finding TextEntry by id via JDBC: {}", id);

        String sql = "SELECT id, title, content, created_at, updated_at FROM text_entries WHERE id = ?";

        List<TextEntry> results = jdbcTemplate.query(sql, new TextEntryRowMapper(), id);

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Retrieves all entries using JDBC template.
     *
     * @return list of all entries
     */
    public List<TextEntry> findAllViaJdbc() {
        log.debug("Finding all TextEntries via JDBC");

        String sql = "SELECT id, title, content, created_at, updated_at FROM text_entries ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, new TextEntryRowMapper());
    }

    /**
     * Searches entries by title or content using JDBC template.
     *
     * @param searchTerm search term
     * @return matching entries
     */
    public List<TextEntry> searchByTitleOrContentViaJdbc(String searchTerm) {
        log.debug("Searching TextEntries by title or content via JDBC: {}", searchTerm);

        String sql = "SELECT id, title, content, created_at, updated_at FROM text_entries " +
                     "WHERE title ILIKE ? OR content ILIKE ?";

        String likePattern = "%" + searchTerm + "%";

        return jdbcTemplate.query(sql, new TextEntryRowMapper(), likePattern, likePattern);
    }

    /**
     * Deletes entry by ID using JDBC template.
     *
     * @param id entry ID to delete
     * @return number of rows affected
     */
    public int deleteByIdViaJdbc(Long id) {
        log.debug("Deleting TextEntry by id via JDBC: {}", id);

        String sql = "DELETE FROM text_entries WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    // ==================== Raw SQL Execution Methods ====================
    // WARNING: These methods are intentionally vulnerable for educational purposes
    // They accept unsanitized SQL and should NEVER be used in production

    /**
     * Executes raw SQL query returning {@link TextEntry} objects.
     * <p>
     * WARNING: Intentionally vulnerable to SQL injection for educational purposes.
     *
     * @param sqlQuery unsanitized SQL query
     * @return list of text entries
     */
    public List<TextEntry> executeRawQuery(String sqlQuery) {
        log.warn("Executing raw SQL query: {}", sqlQuery);

        return jdbcTemplate.query(sqlQuery, new TextEntryRowMapper());
    }

    /**
     * Executes raw SQL query returning list of maps.
     * <p>
     * WARNING: Intentionally vulnerable to SQL injection for educational purposes.
     *
     * @param sqlQuery unsanitized SQL query
     * @return list of maps (column name to value)
     */
    public List<java.util.Map<String, Object>> executeRawQueryForMaps(String sqlQuery) {
        log.warn("Executing raw SQL query for maps: {}", sqlQuery);

        return jdbcTemplate.queryForList(sqlQuery);
    }

    /**
     * Executes raw SQL query returning single map.
     * <p>
     * WARNING: Intentionally vulnerable to SQL injection for educational purposes.
     *
     * @param sqlQuery unsanitized SQL query
     * @return map of column name to value, or null if no results
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
     * Executes raw SQL query returning single value.
     * <p>
     * WARNING: Intentionally vulnerable to SQL injection for educational purposes.
     *
     * @param <T> return type
     * @param sqlQuery unsanitized SQL query
     * @param requiredType class of return type
     * @return query result, or null if no results
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
     * Executes raw SQL update/insert/delete statement.
     * <p>
     * WARNING: Intentionally vulnerable to SQL injection for educational purposes.
     *
     * @param sqlStatement unsanitized SQL statement
     * @return number of rows affected
     */
    public int executeRawUpdate(String sqlStatement) {
        log.warn("Executing raw SQL update: {}", sqlStatement);

        return jdbcTemplate.update(sqlStatement);
    }

    /**
     * Executes raw SQL statement of any type.
     * <p>
     * WARNING: Most dangerous method - executes any SQL without validation.
     * Intentionally vulnerable for educational purposes.
     *
     * @param sqlStatement unsanitized SQL statement
     * @return true if statement returns result set, false otherwise
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
     * Executes multiple raw SQL statements (semicolon-separated).
     * <p>
     * WARNING: Intentionally vulnerable to SQL injection for educational purposes.
     *
     * @param sqlStatements unsanitized SQL statements
     * @return number of statements executed
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

    /**
     * Row mapper for converting {@link ResultSet} to {@link TextEntry}.
     *
     * @author jay
     */
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
