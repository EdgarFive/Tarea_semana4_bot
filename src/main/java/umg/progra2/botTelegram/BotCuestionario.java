package umg.progra2.botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.progra2.model.User;
import umg.progra2.service.ResponseService;
import umg.progra2.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotCuestionario extends TelegramLongPollingBot {

    private Map<Long, String> estadoConversacion = new HashMap<>();

    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();

    //Para lo de las preguntas ==
    private final Map<Long, Boolean> esperandoEdad = new HashMap<>();



    User usuarioConectado = null;
    UserService userService = new UserService();

    @Override
    public String getBotUsername() {
        return "EdgarFiveBot";
    }

    @Override
    public String getBotToken() {
        return "7179472943:AAENQc6mATwpeOuhKYPnjKJf4lYzGd9yo-0";
    }


    @Override
    /* ==============================
    //Verifica si el usuario esta regisrado y si no debe registrarse. tambien analiza si el correo está en la base de datos o no.
    //basicamente es la funcion para analizar cualquier mensaje de texto que mande el usuario.
     */
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) { //Se analizaran los mensajes de texto para definir comandos entre otros ==

                String userFirstName = update.getMessage().getFrom().getFirstName();
                String userLastName = update.getMessage().getFrom().getLastName();
                String nickName = update.getMessage().getFrom().getUserName();
                long chat_id = update.getMessage().getChatId();
                String mensaje_Texto = update.getMessage().getText();

                String state = estadoConversacion.getOrDefault(chat_id, "");
                usuarioConectado = userService.getUserByTelegramId(chat_id);

                // Verificación inicial del usuario, si usuarioConectado es nullo, significa que no tiene registro de su id de telegram en la tabla
                if (usuarioConectado == null && state.isEmpty()) {
                    sendText(chat_id, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + ", no tienes un usuario registrado en el sistema. Por favor ingresa tu correo electrónico:");
                    estadoConversacion.put(chat_id, "ESPERANDO_CORREO");
                    return;
                } else {
                    sendText(chat_id, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + ", bienvenido al sistema BotFive. ¿En qué puedo ayudarte?\n\nPara empezar, puedes elegir cualquiera de las siguientes opciones:\n/menu : Si deseas iniciar el cuestionario.");
                }

                // Manejo del estado ESPERANDO_CORREO
                if (state.equals("ESPERANDO_CORREO")) {
                    processEmailInput(chat_id, mensaje_Texto);
                    return;
                }

                if (mensaje_Texto.equals("/menu")) { //Muestra el menú de preguntas ==
                    sendMenu(chat_id);
                } else if (seccionActiva.containsKey(chat_id)) { //Si estamos en medio de un cuestionario ==
                    manejaCuestionario(chat_id, mensaje_Texto);
                }

            } else if (update.hasCallbackQuery()) { //es una respuesta de un boton
                String callbackData = update.getCallbackQuery().getData();
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                inicioCuestionario(chatId, callbackData);
            } else {
                System.out.println("Tipo de actualización no manejado.");

            }

        } catch (Exception e) {
            long chat_id = update.getMessage() != null ? update.getMessage().getChatId() : 0;
            sendText(chat_id, "Ocurrió un error al procesar tu mensaje. Por favor intenta de nuevo.");
            e.printStackTrace();
        }

    }
/*
    @Override
    //Verifica si hay un mensaje de texto ========
    public void onUpdateReceived(Update actualizacion) {
        if (actualizacion.hasMessage() && actualizacion.getMessage().hasText()) {

            String messageText = actualizacion.getMessage().getText();
            long chatId = actualizacion.getMessage().getChatId();

            if (messageText.equals("/menu")) {
                sendMenu(chatId);
            } else if (seccionActiva.containsKey(chatId)) { //Si estamos en medio de un cuestionario ==
                manejaCuestionario(chatId, messageText);
            }
        } else if (actualizacion.hasCallbackQuery()) { //es una respusta de un boton
            String callbackData = actualizacion.getCallbackQuery().getData();
            long chatId = actualizacion.getCallbackQuery().getMessage().getChatId();
            inicioCuestionario(chatId, callbackData);
        }
    }

 */


    //funcion para formatear la información del usuario
    private String formatUserInfo(String firstName, String lastName, String userName) {
        return firstName + " " + lastName + " (" + userName + ")";
    }

    private String formatUserInfo(long chat_id, String firstName, String lastName, String userName) {
        return chat_id + " " + formatUserInfo(firstName, lastName, userName);
    }


    //verifica si el usurio está registrado en la tabla con su correo electrónico
    private void processEmailInput(long chat_id, String email) {
        sendText(chat_id, "Recibo su Correo: " + email);
        estadoConversacion.remove(chat_id); // Reset del estado
        try{
            usuarioConectado = userService.getUserByEmail(email);
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario por correo: " + e.getMessage());
            e.printStackTrace();
        }


        if (usuarioConectado == null) {
            sendText(chat_id, "El correo no se encuentra registrado en el sistema, por favor contacte al administrador.");
        } else {
            usuarioConectado.setTelegramid(chat_id);
            try {
                userService.updateUser(usuarioConectado);
            } catch (Exception e) {
                System.err.println("Error al actualizar el usuario: " + e.getMessage());
                e.printStackTrace();
            }

            sendText(chat_id, "Usuario actualizado con éxito!");
        }
    }


    //función para enviar mensajes
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


    //===================================================================================

    //===================================================================================

    //===================================================================================

    public BotCuestionario() {
        // Inicializa los cuestionarios con las preguntas.
        preguntas.put("SECTION_1", new String[]{"🤦‍♂️1.1- Estas aburrido?", "😂😂 1.2- Te bañaste hoy?", "🤡🤡 Pregunta 1.3"});
        preguntas.put("SECTION_2", new String[]{"Pregunta 2.1", "Pregunta 2.2", "Pregunta 2.3"});
        preguntas.put("SECTION_3", new String[]{"Pregunta 3.1", "Pregunta 3.2", "Pregunta 3.3"});
        preguntas.put("SECTION_4", new String[]{"4.1- Tienes hambre?", "4.2- Cual es tu edad?", "4.3- Te gustan las galletas?", "4.4- Que tal la clase?","4.5- Como vas en el semestre?", "4.6- Te gusta el café?"});
    }


/* Este metodo se convino con el metodo de arriba que tiene el mismo nombre =====

    @Override
    //Verifica si hay un mensaje de texto ========
    public void onUpdateReceived(Update actualizacion) {
        if (actualizacion.hasMessage() && actualizacion.getMessage().hasText()) {
            String messageText = actualizacion.getMessage().getText();
            long chatId = actualizacion.getMessage().getChatId();

            if (messageText.equals("/menu")) {
                sendMenu(chatId);
            } else if (seccionActiva.containsKey(chatId)) {
                manejaCuestionario(chatId, messageText);
            }
        } else if (actualizacion.hasCallbackQuery()) { //es una respusta de un boton
            String callbackData = actualizacion.getCallbackQuery().getData();
            long chatId = actualizacion.getCallbackQuery().getMessage().getChatId();
            inicioCuestionario(chatId, callbackData);
        }
    }
    */


    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una sección:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Crea los botones del menú
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

    private void inicioCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);

        esperandoEdad.put(chatId, false); //Estado esperanso edad falso ==

        enviarPregunta(chatId);
    }

    private void enviarPregunta(long chatId) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        if (index < questions.length) {
            String eepregunta = questions[index];

            if (seccion.equals("SECTION_4") && index == 1){
                esperandoEdad.put(chatId, true);
            }

            sendText(chatId, questions[index]);
        } else {
            sendText(chatId, "¡Has completado el cuestionario!");
            seccionActiva.remove(chatId);
            indicePregunta.remove(chatId);
            esperandoEdad.remove(chatId);
        }
    }

    private void manejaCuestionario(long chatId, String response) throws SQLException {
        String section = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);

        ResponseService responseService = new ResponseService();


        if(esperandoEdad.getOrDefault(chatId, true)){
            if (validarEdad(response)){
                responseService.guardarResponse(section, chatId, index, response);
                sendText(chatId, "Tu edad es: " + response);
                esperandoEdad.put(chatId, false);
                indicePregunta.put(chatId, index + 1);
                enviarPregunta(chatId);
            }else {
                sendText(chatId, "La edad ingresada no es válida. Por favor, ingresa una edad entre 4 y 100 años.");
            }
        }else{
            responseService.guardarResponse(section, chatId, index, response);
            sendText(chatId, "Tu respuesta fue: " + response);
            indicePregunta.put(chatId, index + 1);
            enviarPregunta(chatId);
        }
    }

    private boolean validarEdad(String edadTexto){
      try{
          int edad = Integer.parseInt(edadTexto);
          return edad >= 4 == edad <= 100;
      }catch (NumberFormatException e){
          return false;
      }
    };

    /*
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
    */

}
