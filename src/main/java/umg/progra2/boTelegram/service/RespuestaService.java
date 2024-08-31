package umg.progra2.boTelegram.service;

import umg.progra2.boTelegram.dao.RespuestaDao;
import umg.progra2.boTelegram.model.Respuesta;
import umg.progra2.boTelegram.model.User;
import umg.progra2.db.DateBaseConnection;
import umg.progra2.db.TransactionManager;


public class RespuestaService {
    private RespuestaDao respuestaDao = new RespuestaDao();


    public RespuestaService() {
        this.respuestaDao = new RespuestaDao();
    }

    // Método para obtener usuario por telegramId
    public Respuesta getUserByTelegramId(long telegramId) {
        return respuestaDao.findUserByTelegramId(telegramId);
    }

    // Método para obtener usuario por email
    public Respuesta getUserByEmail(String email) {
        return respuestaDao.findUserByEmail(email);
    }


    // Método para guardar una respuesta en la base de datos
    public void guardarRespuesta(Respuesta respuesta) {
        respuestaDao.insertRespuesta(respuesta);
    }
    //Método para actualizar el telegramId del usuario
    public void updateUserTelegramId(Respuesta respuesta) {
        respuestaDao.updateUserTelegramId(respuesta);
    }
}
