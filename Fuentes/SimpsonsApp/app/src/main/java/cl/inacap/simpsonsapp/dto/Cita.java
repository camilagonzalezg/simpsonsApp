package cl.inacap.simpsonsapp.dto;

public class Cita {

    private String cita;
    private String personaje;
    private String imagen;
    private String personajeDireccion;

    public String getCita() {
        return cita;
    }

    public void setCita(String cita) {
        this.cita = cita;
    }

    public String getPersonaje() {
        return personaje;
    }

    public void setPersonaje(String personaje) {
        this.personaje = personaje;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPersonajeDireccion() {
        return personajeDireccion;
    }

    public void setPersonajeDireccion(String personajeDireccion) {
        this.personajeDireccion = personajeDireccion;
    }
}
