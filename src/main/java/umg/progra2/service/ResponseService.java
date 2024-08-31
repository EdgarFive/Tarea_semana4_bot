package umg.progra2.service;

import umg.progra2.dao.ResponseDao;
import umg.progra2.model.Response;

import java.sql.SQLException;

public class ResponseService {

    private ResponseDao responseDao = new ResponseDao();

    public void guardarResponse (String seccion, long telegram_id, int pregunta_id, String respuesta_texto) throws SQLException {

        Response resp = new Response();
        resp.setSeccion(seccion);
        resp.setTelegram_id(telegram_id);
        resp.setPregunta_id(pregunta_id);
        resp.setRespuesta_texto(respuesta_texto);

        responseDao.guardarResponse(resp);

    }
}
