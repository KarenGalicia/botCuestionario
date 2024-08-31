package umg.progra2.boTelegram.model;

import java.sql.Timestamp;

public class Respuesta {
        // Campos de usuario

        private long telegramId;
         private int idUsuario;
        private String carne;
        private String nombre;
        private String correo;
        private String seccion;
        private String activo;


        // Campos de respuestas
        private int preguntaId;
        private String respuestaTexto;

        // Constructor para respuestas
        public Respuesta(long telegramId, String seccion, int preguntaId, String respuestaTexto) {
            this.telegramId = telegramId;
            this.seccion = seccion;
            this.preguntaId = preguntaId;
            this.respuestaTexto = respuestaTexto;
        }

        // Constructor para usuario
        public Respuesta(String carne, String nombre, String correo, String seccion, String activo, long telegramId) {
            this.carne = carne;
            this.nombre = nombre;
            this.correo = correo;
            this.seccion = seccion;
            this.activo = activo;
            this.telegramId = telegramId;
        }

    public Respuesta(int idUsuario, String email, long telegramId) {
        }

    // Getters y setters

        // Campos de usuario
        public long getTelegramId() {
            return telegramId;
        }

        public void setTelegramId(long telegramId) {
            this.telegramId = telegramId;
        }
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCarne() {
            return carne;
        }

        public void setCarne(String carne) {
            this.carne = carne;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getSeccion() {
            return seccion;
        }

        public void setSeccion(String seccion) {
            this.seccion = seccion;
        }

        public String getActivo() {
            return activo;
        }

        public void setActivo(String activo) {
            this.activo = activo;
        }

        // Campos de respuestas
        public int getPreguntaId() {
            return preguntaId;
        }

        public void setPreguntaId(int preguntaId) {
            this.preguntaId = preguntaId;
        }

        public String getRespuestaTexto() {
            return respuestaTexto;
        }

        public void setRespuestaTexto(String respuestaTexto) {
            this.respuestaTexto = respuestaTexto;
        }

}