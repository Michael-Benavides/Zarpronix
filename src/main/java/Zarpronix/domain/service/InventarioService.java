package Zarpronix.domain.service;

import Zarpronix.domain.model.Movimiento;
import Zarpronix.domain.model.Producto;
import Zarpronix.infrastructure.persistence.IMovimientoRepository;
import Zarpronix.infrastructure.persistence.IProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioService {

    private final IMovimientoRepository movimientoRepo;
    private final IProductoRepository productoRepo;

    public InventarioService(IMovimientoRepository mov, IProductoRepository prod) {
        this.movimientoRepo = mov;
        this.productoRepo = prod;
    }

    @Transactional
    public String registrarMovimiento(Long productoId, Integer cantidad, String tipo) {
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // 1. Crear el registro en el Kardex (Movimientos)
        Movimiento mov = new Movimiento();
        mov.setProducto(producto);
        mov.setCantidad(cantidad);
        mov.setTipo(tipo.toUpperCase());
        movimientoRepo.save(mov);

        // 2. ACTUALIZAR EL STOCK EN EL PRODUCTO (Esto evita el error del 0)
        int stockActual = producto.getCantidad();
        if ("ENTRADA".equalsIgnoreCase(tipo)) {
            producto.setCantidad(stockActual + cantidad);
        } else {
            producto.setCantidad(stockActual - cantidad);
        }
        
        productoRepo.save(producto); // Guardamos el nuevo balance
        
        return "Éxito: " + tipo + " de " + cantidad + " unidades para " + producto.getNombre();
    }

    public Integer obtenerStockActual(Long productoId) {
        return productoRepo.findById(productoId)
                .map(Producto::getCantidad)
                .orElse(0);
    }
}