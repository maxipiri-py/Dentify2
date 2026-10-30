package db.migration;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V2HashearPasswordsTest {

    private static final String URL = "jdbc:h2:mem:migracion_login;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";

    private Flyway flyway(String target) {
        return Flyway.configure()
                .dataSource(URL, "sa", "")
                .locations("classpath:db/migration")
                .target(target)
                .load();
    }

    @Test
    @DisplayName("V2 -> hashea contraseñas en texto plano y respeta las que ya son BCrypt")
    public void testHasheaPasswordsExistentes() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashExistente = encoder.encode("ya-hasheada");

        // Estado previo: esquema V1 con usuarios guardados como antes del cambio
        flyway("1").migrate();
        try (Connection c = DriverManager.getConnection(URL, "sa", "");
                Statement st = c.createStatement()) {
            st.executeUpdate("insert into usuario (id, username, password, rol) values (1, 'maxi', '1234', 'ADMIN')");
            st.executeUpdate("insert into usuario (id, username, password, rol) values (2, 'ana', '" + hashExistente + "', 'USER')");
            st.executeUpdate("insert into usuario (id, username, password, rol) values (3, 'sinclave', null, 'USER')");
        }

        flyway("latest").migrate();

        try (Connection c = DriverManager.getConnection(URL, "sa", "");
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("select id, password from usuario order by id")) {

            rs.next();
            assertThat(rs.getString("password")).isNotEqualTo("1234");
            assertThat(encoder.matches("1234", rs.getString("password"))).isTrue();

            rs.next();
            assertThat(rs.getString("password")).isEqualTo(hashExistente);

            rs.next();
            assertThat(rs.getString("password")).isNull();
        }
    }
}
