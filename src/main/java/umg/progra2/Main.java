package umg.progra2;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.progra2.boTelegram.tareaBot;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            tareaBot bot = new tareaBot();
            botsApi.registerBot(bot);
            System.out.println("El Bot esta functional....");
        }

        catch(Exception ex){
                System.out.println("error" + ex.getMessage());
            }
        }
    }
