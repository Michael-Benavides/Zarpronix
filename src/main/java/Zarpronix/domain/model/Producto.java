package Zarpronix.domain.model; // Ubicación de la clase dentro de la arquitectura del proyecto

import java.util.List; // Importación para manejar colecciones de objetos

import com.fasterxml.jackson.annotation.JsonIgnore; // Importación para controlar la serialización JSON

import jakarta.persistence.CascadeType; // Define cómo se propagan las operaciones (borrado, actualización)
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table; // Importaciones de JPA para persistencia

@Entity // Define la clase como una entidad de base de datos
@Table(name = "productos") // Nombre de la tabla física en la base de datos
public class Producto {
    @Id // Marca el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID (autoincrementable)
    private Long id;
    
    private String nombre; // Nombre comercial del producto
    private String descripcion; // Detalle o especificaciones del producto
    private Double precio; // Valor unitario del producto
    private Integer cantidad; // Stock actual en inventario
    private String sku; // Código de referencia único (Stock Keeping Unit)
    private String categoria; // Clasificación del producto (ej. Electrónica, Hogar)
    private String ubicacion; // Pasillo, estante o bodega donde se encuentra

    // MODIFICACIÓN: Relación con Movimientos
    // mappedBy: Indica que la relación es gestionada por el campo "producto" en la clase Movimiento
    // cascade: Si se elimina el producto, se eliminan sus movimientos asociados para mantener integridad
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore // Evita la recursividad infinita (Producto -> Movimiento -> Producto...) al convertir a JSON
    private List<Movimiento> movimientos; // Lista que contiene todo el historial de movimientos de este producto

    public Producto() {} // Constructor vacío obligatorio para el funcionamiento de JPA

    // Getters y Setters: Métodos para acceder y modificar las propiedades del objeto

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    // Nuevos Getters y Setters para manejar la colección de movimientos
    public List<Movimiento> getMovimientos() { return movimientos; }
    public void setMovimientos(List<Movimiento> movimientos) { this.movimientos = movimientos; }
}