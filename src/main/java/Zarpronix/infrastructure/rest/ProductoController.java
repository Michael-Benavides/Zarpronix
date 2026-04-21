package Zarpronix.infrastructure.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Zarpronix.domain.model.Producto;
import Zarpronix.infrastructure.persistence.IProductoRepository;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@SuppressWarnings("null") // Elimina las advertencias de "Null type safety"
public class ProductoController {

    @Autowired
    private IProductoRepository productoRepo;

    @GetMapping
    public List<Producto> listar() {
        return productoRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        if (id == null) return ResponseEntity.badRequest().build();
        return productoRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        if (producto.getCantidad() == null) producto.setCantidad(0);
        if (producto.getPrecio() == null) producto.setPrecio(0.0);
        return productoRepo.save(producto);
    }

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
            return ResponseEntity.ok(productoRepo.save(productoExistente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (id == null || !productoRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}