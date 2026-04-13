package Zarpronix.infrastructure.rest;

import Zarpronix.domain.model.Producto;
import Zarpronix.infrastructure.persistence.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional; // <--- NUEVO IMPORT

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private IProductoRepository productoRepo;

    // 1. LISTAR TODOS LOS PRODUCTOS
    @GetMapping
    public List<Producto> listar() {
        return productoRepo.findAll();
    }

    // 2. BUSCAR UN PRODUCTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CREAR NUEVO PRODUCTO (POST)
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        if (producto.getCantidad() == null) {
            producto.setCantidad(0);
        }
        return productoRepo.save(producto);
    }

    // 4. ACTUALIZAR PRODUCTO EXISTENTE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datosNuevos) {
        return productoRepo.findById(id).map(productoExistente -> {
            productoExistente.setSku(datosNuevos.getSku());
            productoExistente.setNombre(datosNuevos.getNombre());
            productoExistente.setCategoria(datosNuevos.getCategoria());
            productoExistente.setPrecio(datosNuevos.getPrecio());
            productoExistente.setUbicacion(datosNuevos.getUbicacion());

            if (datosNuevos.getCantidad() != null) {
                productoExistente.setCantidad(datosNuevos.getCantidad());
            }

            Producto actualizado = productoRepo.save(productoExistente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. ELIMINAR PRODUCTO (MODIFICADO CON TRANSACCIÓN)
    @Transactional // <--- MODIFICACIÓN CLAVE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!productoRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            productoRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}