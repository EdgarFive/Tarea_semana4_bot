package umg.progra2.botTelegram;

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

            //manejo de mensajes
            if(message_text.toLowerCase().equals("hola")){
                sendText(chat_id,"Hola "+nombre+"\nComandos que puedes usar:\n/info\n/progra\n/hola\n/cambio\n/grupal");
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

            //Dar la fecha y hora del sistema ===========================================
            if(message_text.toLowerCase().equals("/hola")){

                sendText(chat_id,"Hola "+nombre+" hoy es: " + MMfecha());
            }

            //Cambio de moneda =================================================0
            if(message_text.startsWith("/cambio")){
                String[] partes = message_text.split(" ");
                if(partes.length == 2){
                    try{

                        double valor1 = Double.parseDouble(partes[1]);
                        double valor2 = valor1 * 8.54;

                        DecimalFormat eeformato = new DecimalFormat("0.00");
                        String eevalor1 = eeformato.format(valor1);
                        String eevalor2 = eeformato.format(valor2);

                        sendText(chat_id,"La cantidad de "+eevalor1+" Euros, equivale a la cantidad de "+eevalor2+" Quetzales.");

                    }catch (NumberFormatException e){
                        sendText(chat_id, "La cantidad ingresada no es válida.");
                    }
                    }else{
                    sendText(chat_id,"Lo siento. Debe colocar una cantidad para hacer el cambio de Euros a Quetzales.");
                }
            }

            //Grupal
            if(message_text.startsWith("/grupal")){
                String[] partes = message_text.split(" ", 2);
                if(partes.length == 2){
                    String eemensaje = partes [1];
                    mensajegrupal(eemensaje);

                }else{
                    sendText(chat_id,"Lo siento. Debe ingresar un mensaje para poder enviarlo.");
                }
            }

            System.out.println("User id: "+chat_id+" Message: " + message_text);
        }
    }



    //==================================================================================
    //Metodos===========================================================================
    //==================================================================================

    public void mensajegrupal (String mensaje){
        for (Long chatId : listaChatIds){
            sendText(chatId, mensaje);
        }
    }

    private static final List<Long> listaChatIds = List.of(
            6699823249L, //Nayeli.
            5792621349L, //Edgar.
            6108736830L, //Marvin.
            6602268509L //Manuel.
    );

    private String MMfecha() {
        SimpleDateFormat fech = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        return fech.format(new Date());
    };

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
