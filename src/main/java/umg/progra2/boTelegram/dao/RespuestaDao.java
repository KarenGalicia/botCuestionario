package umg.progra2.boTelegram.dao;


import umg.progra2.boTelegram.model.Respuesta;
import umg.progra2.db.DateBaseConnection;
import java.sql.*;


public class RespuestaDao {

    // Método para encontrar un usuario por telegramId
    public Respuesta findUserByTelegramId(long telegramId) {
        Respuesta Respuesta = null;
        String sql = "SELECT * FROM usuarios WHERE telegram_id = ?";

        try (Connection conn = DateBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, telegramId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Respuesta = new Respuesta(rs.getInt("id_usuario"), rs.getString("email"), rs.getLong("telegram_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Respuesta;
    }

    // Método para encontrar un usuario por email
    public Respuesta findUserByEmail(String email) {
        Respuesta respuesta = null;
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection conn = DateBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                respuesta = new Respuesta(rs.getInt("id_usuario"), rs.getString("email"), rs.getLong("telegram_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respuesta;
    }

    // Método para actualizar el telegramId del usuario
    public void updateUserTelegramId(Respuesta user) {
        String sql = "UPDATE usuarios SET telegram_id = ? WHERE id_usuario = ?";

        try (Connection conn = DateBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, user.getTelegramId()); // Establece el telegramId
            pstmt.setInt(2, user.getIdUsuario());   // Establece el id_usuario

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para insertar una respuesta en la base de datos
    public void insertRespuesta(Respuesta respuesta) {
        String sql = "INSERT INTO tb_respuestas (telegram_id, seccion, pregunta_id, respuesta_texto, fecha_respuesta) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DateBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, respuesta.getTelegramId());
            pstmt.setString(2, respuesta.getSeccion());
            pstmt.setInt(3, respuesta.getPreguntaId());
            pstmt.setString(4, respuesta.getRespuestaTexto());
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // Fecha actual

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}