package umg.progra2.model;

import java.security.Timestamp;

public class Response {

    private int id;
    private String seccion;
    private long telegram_id;
    private int pregunta_id;
    private String respuesta_texto;
    private Timestamp fecha_respuesta;

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTelegram_id() {
        return telegram_id;
    }

    public void setTelegram_id(long telegram_id) {
        this.telegram_id = telegram_id;
    }

    public int getPregunta_id() {
        return pregunta_id;
    }

    public void setPregunta_id(int pregunta_id) {
        this.pregunta_id = pregunta_id;
    }

    public String getRespuesta_texto() {
        return respuesta_texto;
    }

    public void setRespuesta_texto(String respuesta_texto) {
        this.respuesta_texto = respuesta_texto;
    }

    public Timestamp getFecha_respuesta() {
        return fecha_respuesta;
    }

    public void setFecha_respuesta(Timestamp fecha_respuesta) {
        this.fecha_respuesta = fecha_respuesta;
    }

}
