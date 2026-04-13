package Zarpronix.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;

@Entity
@Data
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // <--- MODIFICACIÓN CLAVE
    private Producto producto;

    private Integer cantidad;
    private String tipo; // ENTRADA o SALIDA
    private LocalDateTime fecha;

    // --- MÉTODOS MANUALES ---
    public void setProducto(Producto producto) { this.producto = producto; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Producto getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public String getTipo() { return tipo; }
}