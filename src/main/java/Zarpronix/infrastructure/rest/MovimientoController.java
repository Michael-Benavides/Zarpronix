package Zarpronix.infrastructure.rest;

import Zarpronix.domain.service.InventarioService;
import Zarpronix.infrastructure.persistence.IProductoRepository;
import Zarpronix.domain.model.Producto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {

    private final InventarioService inventarioService;
    private final IProductoRepository productoRepo;

    public MovimientoController(InventarioService service, IProductoRepository repo) {
        this.inventarioService = service;
        this.productoRepo = repo;
    }

    // Registra la entrada o salida física
    @PostMapping("/{productoId}/{tipo}/{cantidad}")
    public String procesar(@PathVariable Long productoId, @PathVariable String tipo, @PathVariable Integer cantidad) {
        return inventarioService.registrarMovimiento(productoId, cantidad, tipo);
    }

    // Consulta el stock actual para la tabla
    @GetMapping("/stock/{productoId}")
    public Integer verStock(@PathVariable Long productoId) {
        return inventarioService.obtenerStockActual(productoId);
    }

    // NUEVO: Calcula el total para la factura (Precio x Cantidad)
    @GetMapping("/factura/{productoId}/{cantidad}")
    public Double calcularFactura(@PathVariable Long productoId, @PathVariable Integer cantidad) {
        Producto p = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Si el precio es nulo, devolvemos 0 para evitar errores
        Double precio = (p.getPrecio() != null) ? p.getPrecio() : 0.0;
        return precio * cantidad;
    }
}