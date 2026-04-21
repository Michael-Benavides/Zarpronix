package Zarpronix.domain.model; // Define la ubicación de la clase dentro de la arquitectura del proyecto

import jakarta.persistence.Entity; // Importa las anotaciones necesarias para la persistencia de datos con Jakarta EE
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Indica que esta clase es una entidad que se mapeará con una tabla en la base de datos
@Table(name = "clientes") // Especifica explícitamente que la tabla en la base de datos se llama "clientes"
public class Cliente {
    
    @Id // Define el atributo 'id' como la llave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura el autoincremento para que la base de datos genere el ID automáticamente
    private Long id; 
    
    private String nombre; // Atributo para almacenar el nombre completo del cliente
    private String cedula; // Atributo para el documento de identidad o cédula
    private String telefono; // Atributo para el número de contacto
    private String email; // Atributo para la dirección de correo electrónico

    // Constructor vacío (Obligatorio para JPA): Permite a Hibernate/JPA crear instancias de la clase mediante reflexión
    public Cliente() {}

    // Getters y Setters Manuales: Métodos para permitir el acceso y la modificación de los datos privados

    public Long getId() { return id; } // Obtiene el valor del ID
    public void setId(Long id) { this.id = id; } // Asigna un valor al ID

    public String getNombre() { return nombre; } // Obtiene el nombre
    public void setNombre(String nombre) { this.nombre = nombre; } // Asigna el nombre

    public String getCedula() { return cedula; } // Obtiene la cédula
    public void setCedula(String cedula) { this.cedula = cedula; } // Asigna la cédula

    public String getTelefono() { return telefono; } // Obtiene el teléfono
    public void setTelefono(String telefono) { this.telefono = telefono; } // Asigna el teléfono

    public String getEmail() { return email; } // Obtiene el email
    public void setEmail(String email) { this.email = email; } // Asigna el email
}