package Zarpronix.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String nombre;
    private String categoria;
    private Double precio;
    private String ubicacion;
    private Integer cantidad; // <--- ESTA ES LA QUE FALTABA

    // --- MÉTODOS MANUALES (Sincronizados) ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    // NUEVOS MÉTODOS PARA EL STOCK
    public Integer getCantidad() { 
        return (cantidad == null) ? 0 : cantidad; 
    }

    public void setCantidad(Integer cantidad) { 
        this.cantidad = cantidad; 
    }
}