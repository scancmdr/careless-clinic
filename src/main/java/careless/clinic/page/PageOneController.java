package careless.clinic.page;

import careless.clinic.model.TextEntry;
import careless.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for page one with intentionally vulnerable SQL injection demonstration.
 * <p>
 * WARNING: Contains insecure code for educational purposes only.
 *
 * @author jay
 * @see TextEntry
 */
@Slf4j
@Controller
@RequestMapping("/page-one")
public class PageOneController {

    @Autowired
    private DataSource dataSource;

    /**
     * Displays page one with all text entries.
     *
     * @param model Spring MVC model
     * @return view name "page-one"
     */
    @GetMapping()
    public String pageOne(Model model) {
        model.addAttribute("entries", getAllEntries());
        return "page-one";
    }

    /**
     * Handles text submission via SQL injection vulnerable method.
     *
     * @param text user-submitted text
     * @return redirect to /page-one
     */
    @PostMapping("/submit")
    public String submitText(@RequestParam("text") String text) {
        try {
            save(text);
        } catch (Exception e) {
            safeSave("error",
                    "This form controller is intentionally insecure. If you see this message you posted text "
                            +"that mangled the construction of the SQL insert statement, and the CRS anomaly score likely "
                            +"wasn't high enough to trigger a block."
                            +" error was:"+e.getMessage()); //Tools.getStackTraceAsString(e));
            log.error("error saving text entry", e);
        }
        return "redirect:/page-one";
    }

    /**
     * Deletes an entry by ID.
     *
     * @param id entry ID to delete
     * @return redirect to /page-one
     */
    @PostMapping("/delete")
    public String deleteEntry(@RequestParam("id") Long id) {
        deleteById(id);
        return "redirect:/page-one";
    }

    /**
     * Saves text using vulnerable string concatenation (SQL injection risk).
     *
     * @param text user-submitted text
     * @throws Exception if save fails
     */
    private void save(String text) throws Exception {
        String ts = Tools.now();
        String sql = "INSERT INTO text_entries (content, created_at, updated_at) VALUES ('" + text + "', '" + ts + "', '" + ts + "')";
        log.info("saveEntry for: {}", sql);
        try (Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    /**
     * Saves text securely using prepared statements.
     *
     * @param title entry title
     * @param content entry content
     */
    private void safeSave(String title, String content) {
        String sql = "INSERT INTO text_entries (title, content, created_at, updated_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving text entry", e);
        }
    }

    /**
     * Retrieves all text entries from the database.
     *
     * @return list of all {@link TextEntry} objects
     */
    private List<TextEntry> getAllEntries() {
        List<TextEntry> entries = new ArrayList<>();
        String sql = "SELECT id, title, content, created_at, updated_at FROM text_entries ORDER BY created_at DESC";

        // Create table if it doesn't exist
        createTableIfNotExists();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TextEntry entry = TextEntry.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .build();
                entries.add(entry);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving text entries", e);
        }

        return entries;
    }

    /**
     * Creates the text_entries table if it doesn't exist.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS text_entries (" +
                "id SERIAL PRIMARY KEY, " +
                "title TEXT NOT NULL, " +
                "content TEXT, " +
                "created_at TIMESTAMP NOT NULL, " +
                "updated_at TIMESTAMP NOT NULL)";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    /**
     * Deletes a text entry by ID using prepared statement.
     *
     * @param id entry ID to delete
     */
    private void deleteById(Long id) {
        String sql = "DELETE FROM text_entries WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting text entry", e);
        }
    }
}
