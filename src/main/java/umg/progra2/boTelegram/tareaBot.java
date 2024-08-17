package umg.progra2.boTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class tareaBot extends TelegramLongPollingBot {
    private static final double TIPO_CAMBIO = 8.88;

    private static final String COMMANDS = "Comandos disponibles:\n" +
            "/start - Inicia interacción con el bot y muestra este mensaje.\n" +
            "/info - Muestra información del estudiante.\n" +
            "/progra - Comentarios sobre la clase de programación.\n" +
            "/hola - Saluda al usuario y muestra la fecha actual.\n" +
            "/cambio [cantidad] - Convierte la cantidad especificada de euros a quetzales.\n" +
            "/grupal [mensajeGrupo1] - Envía un mensaje grupal a todos los usuarios permitidos.\n";


    private static final List<Long> COMPANEROS_CHAT_IDS = List.of(
            5747730047L,
            6602268509L,
            6699823249L,
            5454689659L
    );

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
        String nombre = update.getMessage().getFrom().getFirstName();
        String apellido = update.getMessage().getFrom().getLastName();
        String nickName = update.getMessage().getFrom().getUserName();

        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Hola" + nickName + "Tu nombre es:" + nombre + " y tu apellido es:" + apellido);
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            try {
                // Manejo de mensajes
                if (message_text.equals("/start") || message_text.equals("/help")) {
                    sendTextMessage(chat_id, COMMANDS);
                } else if (message_text.toLowerCase().equals("/info")) {
                    sendTextMessage(chat_id, "Número de carnet: 0905-23-7626, Nombre: Karen Galicia, Semestre: 4");
                } else if (message_text.toLowerCase().equals("/progra")) {
                    sendTextMessage(chat_id, "¡En nuestra clase de programación hemos estado explorando varios conceptos fundamentales, desde la estructura de los programas hasta la resolución de problemas mediante código. Hemos trabajado en proyectos prácticos y realizado ejercicios interactivos para aplicar lo que hemos aprendido.");
                } else if (message_text.toLowerCase().equals("/hola")) {
                    // Obtén la fecha y la hora del sistema
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd 'de' MMMM yyyy", new Locale("es", "ES"));
                    String fechaActual = dateFormat.format(new Date());

                    // Forma el mensaje
                    String saludo = String.format("Hola %s, gusto de saludarte. Hoy es %s.", nombre, fechaActual);

                    // Envía el mensaje
                    sendTextMessage(chat_id, saludo);
                } else if (message_text.startsWith("/cambio")) {
                    try {
                        // Extrae la cantidad de Euros del mensaje
                        String cantidadTexto = message_text.substring(8).trim();
                        double cantidadEuros = Double.parseDouble(cantidadTexto);
                        double cantidadQuetzales = cantidadEuros * TIPO_CAMBIO;

                        // Formatea el resultado
                        DecimalFormat formato = new DecimalFormat("#.##");
                        String resultado = String.format("Son %s euros.", formato.format(cantidadQuetzales));

                        // Envía el mensaje
                        sendTextMessage(chat_id, resultado);
                    } catch (NumberFormatException e) {
                        sendTextMessage(chat_id, "Por favor, ingresa una cantidad válida en Euros.");
                    }
                } else if (message_text.toLowerCase().startsWith("/grupal")) {
                    String mensajeGrupal = message_text.substring(8).trim();

                    // Enviar el mensaje a todos los compañeros en la lista
                    for (Long companeroChatId : COMPANEROS_CHAT_IDS) {
                        try {
                            sendTextMessage(companeroChatId, mensajeGrupal);
                        } catch (TelegramApiException e) {
                            // Maneja errores específicos al enviar mensajes
                            e.printStackTrace();
                        }
                    }
                    sendTextMessage(chat_id, "El mensaje ha sido enviado a tus compañeros.");
                } else {
                    sendTextMessage(chat_id, "Comando no reconocido.\n" + COMMANDS);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
                try {
                    sendTextMessage(chat_id, "Ha ocurrido un error con el API de Telegram.");
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void sendTextMessage(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        execute(message);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Error al enviar el mensaje: " + e.getMessage());
            throw e;  // Opcional: Vuelve a lanzar la excepción si necesitas manejarla más arriba
        }
    }
}



