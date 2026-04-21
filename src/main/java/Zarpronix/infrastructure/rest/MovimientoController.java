package Zarpronix.infrastructure.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Zarpronix.domain.model.Producto;
import Zarpronix.domain.service.InventarioService;
import Zarpronix.infrastructure.persistence.IProductoRepository;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
@SuppressWarnings("null")
public class MovimientoController {

    private final InventarioService inventarioService;
    private final IProductoRepository productoRepo;

    public MovimientoController(InventarioService service, IProductoRepository repo) {
        this.inventarioService = service;
        this.productoRepo = repo;
    }

    @PostMapping("/{productoId}/{tipo}/{cantidad}")
    public String procesar(@PathVariable Long productoId, @PathVariable String tipo, @PathVariable Integer cantidad) {
        return inventarioService.registrarMovimiento(productoId, cantidad, tipo);
    }

    @GetMapping("/stock/{productoId}")
    public Integer verStock(@PathVariable Long productoId) {
        return inventarioService.obtenerStockActual(productoId);
    }

    @GetMapping("/factura/{productoId}/{cantidad}")
    public Double calcularFactura(@PathVariable Long productoId, @PathVariable Integer cantidad) {
        Producto p = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Double precio = (p.getPrecio() != null) ? p.getPrecio() : 0.0;
        return precio * cantidad;
    }
}