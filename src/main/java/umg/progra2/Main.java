package umg.progra2;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.progra2.botTelegram.Bot;
import umg.progra2.botTelegram.PokemonBot;
import umg.progra2.botTelegram.tareaBot;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        try{

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            //PokemonBot poke = new PokemonBot();
            tareaBot tare = new tareaBot();

            botsApi.registerBot(tare);

            System.out.println("El Bot está funcionando...");

        }catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }

        /*
        try{

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            PokemonBot poke = new PokemonBot();

            botsApi.registerBot(poke);

            System.out.println("El Bot está funcionando...");

        }catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
        */

        /*
        try{

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            Bot bot = new Bot();
            botsApi.registerBot(bot);

            System.out.println("El Bot está funcionando...");

        }catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
        */


    }
}