package umg.progra2.boTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.progra2.boTelegram.model.Respuesta;
import umg.progra2.boTelegram.model.User;
import umg.progra2.boTelegram.service.RespuestaService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class botCuestionario extends TelegramLongPollingBot {
    private  Map<Long, Integer> indicePregunta = new HashMap<>();
    private  Map<Long, String> seccionActiva = new HashMap<>();
    private  Map<String, String[]> preguntas = new HashMap<>();
    private  Map<Long, String> estadoConversacion = new HashMap<>();
    private  RespuestaService userService = new RespuestaService();
    private RespuestaService RespuestaService = new RespuestaService();

    private User usuarioConectado = null;

    public botCuestionario() {
        preguntas.put("SECTION_1", new String[]{"🤦‍♂️1.1- Estas aburrido?", "😂😂 1.2- Te bañaste hoy?", "🤡🤡 Pregunta 1.3"});
        preguntas.put("SECTION_2", new String[]{" 2.1", "Pregunta 2.2- ¿Cuántos años tienes?", "Pregunta 2.3"});
        preguntas.put("SECTION_3", new String[]{"Pregunta 3.1", "Pregunta 3.2", "Pregunta 3.3"});
        preguntas.put("SECTION_4", new String[]{"Pregunta 4.1- ¿Cómo te sientes?", "Pregunta 4.2- ¿Cuál es tu edad?", "Pregunta 4.3- ¿Quieres que te apoye en algo?"});
    }

    @Override
    public String getBotUsername() {
        return "@gali4_bot";
    }

    @Override
    public String getBotToken() {
        return "7438230096:AAEKXy8RWXX-OvoTi9sr2t8NTWJpBgVl88Q";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            System.out.println("Mensaje recibido: " + messageText + " de chatId: " + chatId);

            if (estadoConversacion.containsKey(chatId)) {
                manejarRegistro(chatId, messageText);
            } else {
                verificarRegistro(chatId, messageText, update);
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            iniciarCuestionario(chatId, callbackData);
        }
    }

    private void verificarRegistro(long chatId, String messageText, Update update) {
        String userFirstName = update.getMessage().getFrom().getFirstName();
        String userLastName = update.getMessage().getFrom().getLastName();
        String nickName = update.getMessage().getFrom().getUserName();

        try {
            Respuesta usuarioConectado = RespuestaService.getUserByTelegramId(chatId);  // Ahora es del tipo Respuesta

            if (usuarioConectado == null) {
                sendText(chatId, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + ", no tienes un usuario registrado. Ingresa tu correo electrónico:");
                estadoConversacion.put(chatId, "ESPERANDO_CORREO");
            } else {
                sendMenu(chatId); // Enviar menú solo si el usuario ya está registrado
            }
        } catch (Exception e) {
            sendText(chatId, "Ocurrió un error al verificar tu registro.");
        }
    }
    private void manejarRegistro(long chatId, String messageText) {
        if (estadoConversacion.get(chatId).equals("ESPERANDO_CORREO")) {
            processEmailInput(chatId, messageText);
        } else {
            manejaCuestionario(chatId, messageText);
        }
    }

    private void processEmailInput(long chatId, String email) {
        sendText(chatId, "Recibido el correo: " + email);
        estadoConversacion.remove(chatId);
        try {
            Respuesta usuarioConectado = RespuestaService.getUserByEmail(email);
            if (usuarioConectado != null) {
                usuarioConectado.setTelegramId(chatId);
                RespuestaService.updateUserTelegramId(usuarioConectado);
                sendText(chatId, "Usuario registrado con éxito. Envía /menu para iniciar el cuestionario.");
            } else {
                sendText(chatId, "El correo no está registrado.");
            }
        } catch (Exception e) {
            sendText(chatId, "Error al procesar el correo.");
        }
    }


    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una sección:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(crearFilaBoton("Sección 1", "SECTION_1"));
        rows.add(crearFilaBoton("Sección 2", "SECTION_2"));
        rows.add(crearFilaBoton("Sección 3", "SECTION_3"));
        rows.add(crearFilaBoton("Sección 4", "SECTION_4"));
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<InlineKeyboardButton> crearFilaBoton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    private void iniciarCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);
        enviarPregunta(chatId);
    }

    private void enviarPregunta(long chatId) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        if (index < questions.length) {
            sendText(chatId, questions[index]);
        } else {
            sendText(chatId, "¡Has completado el cuestionario!");
            seccionActiva.remove(chatId);
            indicePregunta.remove(chatId);
        }
    }

    private void manejaCuestionario(long chatId, String response) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);

        if (seccion.equals("SECTION_4") && index == 1) {
            try {
                int edad = Integer.parseInt(response);
                if (edad >= 18 && edad <= 99) {
                    guardarRespuesta(chatId, seccion, index, response);
                    sendText(chatId, "Tu respuesta fue: " + response);
                    indicePregunta.put(chatId, index + 1);
                    enviarPregunta(chatId);
                } else {
                    sendText(chatId, "Por favor, ingresa una edad válida.");
                }
            } catch (NumberFormatException e) {
                sendText(chatId, "Por favor, ingresa un número válido.");
            }
        } else {
            guardarRespuesta(chatId, seccion, index, response);
            sendText(chatId, "Tu respuesta fue: " + response);
            indicePregunta.put(chatId, index + 1);
            enviarPregunta(chatId);
        }
    }

    private void guardarRespuesta(long chatId, String seccion, int preguntaId, String respuestaTexto) {
        Respuesta respuesta = new Respuesta(chatId, seccion, preguntaId, respuestaTexto);
        RespuestaService.guardarRespuesta(respuesta);
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String formatUserInfo(String firstName, String lastName, String nickName) {
        return (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "") +
                (nickName != null ? " (@" + nickName + ")" : "");
    }

}
