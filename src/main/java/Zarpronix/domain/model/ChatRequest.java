package Zarpronix.domain.model; // Declaración del paquete que organiza la estructura del proyecto

public class ChatRequest { // Definición de la clase pública ChatRequest
    
    private String mensaje; // Variable privada para almacenar el texto del mensaje enviado por el usuario
    
    // Getter y Setter: Métodos públicos para acceder y modificar el atributo privado
    
    public String getMensaje() { 
        return mensaje; // Retorna el contenido actual de la variable mensaje
    }
    
    public void setMensaje(String mensaje) { 
        this.mensaje = mensaje; // Asigna un nuevo valor a la variable mensaje de esta clase
    }
}