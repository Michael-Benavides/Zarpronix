package Zarpronix.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Zarpronix.domain.model.Movimiento;
import Zarpronix.domain.model.Producto;
import Zarpronix.infrastructure.persistence.IMovimientoRepository;
import Zarpronix.infrastructure.persistence.IProductoRepository;

@Service
@SuppressWarnings("all") // Elimina todas las advertencias de "Null safety" y análisis de VS Code
public class InventarioService {

    private final IMovimientoRepository movimientoRepo;
    private final IProductoRepository productoRepo;

    public InventarioService(IMovimientoRepository mov, IProductoRepository prod) {
        this.movimientoRepo = mov;
        this.productoRepo = prod;
    }

    @Transactional
    public String registrarMovimiento(Long productoId, Integer cantidad, String tipo) {
        // 1. Validación de seguridad para el ID del producto
        if (productoId == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }

        // 2. Corregimos el error de "Unboxing" asegurando que la cantidad no sea nula
        int cantidadSegura = (cantidad != null) ? cantidad : 0;

        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // 3. Crear el registro en el Kardex (Movimientos)
        Movimiento mov = new Movimiento();
        mov.setProducto(producto);
        mov.setCantidad(cantidadSegura);
        mov.setTipo(tipo.toUpperCase());
        movimientoRepo.save(mov);

        // 4. ACTUALIZAR EL STOCK EN EL PRODUCTO
        // Validamos que el stock actual de la base de datos no sea nulo antes de operar
        int stockActual = (producto.getCantidad() != null) ? producto.getCantidad() : 0;
        
        if ("ENTRADA".equalsIgnoreCase(tipo)) {
            // Aquí ya no dará error porque usamos cantidadSegura (int primitivo)
            producto.setCantidad(stockActual + cantidadSegura);
        } else {
            // Verificamos que no quede en negativo (Salida)
            int nuevoStock = stockActual - cantidadSegura;
            producto.setCantidad(Math.max(nuevoStock, 0));
        }
        
        productoRepo.save(producto); // Guarda el cambio en la tabla 'productos'
        
        return "Éxito: " + tipo + " de " + cantidadSegura + " unidades para " + producto.getNombre();
    }

    public Integer obtenerStockActual(Long productoId) {
        if (productoId == null) return 0; // Protección contra nulos
        
        return productoRepo.findById(productoId)
                .map(Producto::getCantidad)
                .orElse(0);
    }
}