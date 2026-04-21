package Zarpronix.domain.model; // Ubicación de la clase dentro del paquete de modelos del dominio

import java.time.LocalDateTime; // Importación para manejar fechas y horas precisas

import jakarta.persistence.Entity; // Define que la clase es una entidad persistente
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table; // Importaciones de JPA para el mapeo a base de datos

@Entity // Marca la clase para que JPA la reconozca como una tabla
@Table(name = "movimientos") // Define el nombre de la tabla en la base de datos
public class Movimiento {
    @Id // Define el campo 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID por la base de datos
    private Long id;
    
    private String tipo; // Almacena el tipo de operación: "ENTRADA" o "SALIDA"
    private Integer cantidad; // Cantidad de unidades involucradas en el movimiento
    
    // MODIFICACIÓN: Se inicializa con la fecha y hora actual del sistema al momento de crear el objeto
    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne // Define una relación de muchos a uno (varios movimientos pertenecen a un solo producto)
    @JoinColumn(name = "producto_id", nullable = false) // Mapea la llave foránea y asegura que el campo sea obligatorio
    private Producto producto; // Referencia al objeto Producto relacionado

    public Movimiento() {} // Constructor por defecto requerido por JPA

    // Getters y Setters: Métodos para acceder y modificar los atributos de la clase

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}