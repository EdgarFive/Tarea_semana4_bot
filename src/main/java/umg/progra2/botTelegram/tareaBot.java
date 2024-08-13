package umg.progra2.botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class tareaBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "EdgarFiveBot";
    }

    @Override
    public String getBotToken() {
        return "7179472943:AAENQc6mATwpeOuhKYPnjKJf4lYzGd9yo-0";
    }


    //El método onUpdateReceived(Update update) de la clase Bot se usa para manejar todas las actualizaciones que el
    // bot recibe.
    // Dependiendo del tipo de actualización, se toman diferentes acciones.

    @Override
    public void onUpdateReceived(Update update) {

        //obtener informacion de la persona que manda los mensajes.
        String nombre = update.getMessage().getFrom().getFirstName();
        String apellido = update.getMessage().getFrom().getLastName();
        String nickName = update.getMessage().getFrom().getUserName();

        //Se verifica si la actualización contiene un mensaje y si ese mensaje tiene texto.
        //Luego se procesa el contenido del mensaje y se responde según el caso.
        if (update.hasMessage() && update.getMessage().hasText()) {

            System.out.println("Hola "+nickName+ " Tu nombre es: " +nombre+ " y tu apellido es: "+apellido);
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String[] alumn1 = {"0905-23-13243", "Edgar Chinchilla","Cuarto ciclo de Ingeniería en Sistemas"};
            String[] alumn2 = {"0905-23-13243", "manuel monzón","Cuarto ciclo de Ingeniería en Sistemas"};

            //manejo de mensajes
            if(message_text.toLowerCase().equals("hola")){
                sendText(chat_id,"Hola "+nombre+"\n/info\n/progra\n");
            }

            //Comando para información (/info)=====================================
            if(message_text.toLowerCase().equals("/info")){
                sendText(chat_id,"Numero de Carnet: 0905-23-13243\nNombre: Edgar Guillermo Chinchilla Chinchilla.\nSemestre: Cuarto ciclo de Ingeniería en Sistemas.");

                /*
                if (nickName == "@EdgarFive"){
                    sendText(chat_id,"Numero de Carnet:"+alumn2[0]+"\nNombre:"+alumn2[1]+"\nSemestre:"+alumn2[2]);
                };
                */

            }


            //Comentario sobre la clase de programación (/progra) ========================
            if(message_text.toLowerCase().equals("/progra")){
                sendText(chat_id,"La clase de programación me parece la más entretenida de todas las clases que llevo en la universidad.\nEs muy dinámica y en momentos divertidad. Se aprende mucho con el ingeniero Ruldyn pues se explica muy bien.\n\n En resumen, es mi clase favorita.");
            }

            System.out.println("User id: "+chat_id+" Message: " + message_text);
        }
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

}
