package db.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Convierte las contraseñas guardadas en texto plano a hash BCrypt,
 * para que los usuarios existentes sigan entrando con su misma contraseña.
 * Las que ya son BCrypt se dejan tal cual.
 */
public class V2__hashear_passwords extends BaseJavaMigration {

    private static final Pattern BCRYPT = Pattern.compile("\\A\\$2([ayb])?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @Override
    public void migrate(Context context) throws Exception {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Connection connection = context.getConnection();

        try (Statement select = connection.createStatement();
                ResultSet rs = select.executeQuery("select id, password from usuario where password is not null");
                PreparedStatement update = connection.prepareStatement("update usuario set password = ? where id = ?")) {

            while (rs.next()) {
                String password = rs.getString("password");
                if (BCRYPT.matcher(password).matches()) {
                    continue;
                }
                update.setString(1, encoder.encode(password));
                update.setInt(2, rs.getInt("id"));
                update.addBatch();
            }
            update.executeBatch();
        }
    }
}
