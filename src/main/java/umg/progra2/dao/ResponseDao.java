package umg.progra2.dao;

import umg.progra2.db.DatabaseConnection;
import umg.progra2.model.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponseDao {

    public void guardarResponse(Response response) throws SQLException {
        String query = "INSERT INTO tb_respuestas (seccion, telegram_id, pregunta_id, respuesta_texto) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, response.getSeccion());
            statement.setLong(2, response.getTelegram_id());
            statement.setInt(3, response.getPregunta_id());
            statement.setString(4, response.getRespuesta_texto());

            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores apropiado
        }
    }
}
