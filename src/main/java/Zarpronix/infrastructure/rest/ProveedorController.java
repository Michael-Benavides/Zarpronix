package Zarpronix.infrastructure.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Zarpronix.domain.model.Proveedor;
import Zarpronix.infrastructure.persistence.ProveedorRepository;

@RestController
@RequestMapping("/proveedores")
@CrossOrigin(origins = "*")
@SuppressWarnings("all")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("/listar")
    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    @PostMapping("/guardar")
    public Proveedor guardarProveedor(@RequestBody Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // NUEVO: Método para eliminar proveedor
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        proveedorRepository.deleteById(id);
    }

    // NUEVO: Método para actualizar proveedor
    @PutMapping("/actualizar/{id}")
    public Proveedor actualizar(@PathVariable Long id, @RequestBody Proveedor nuevo) {
        return proveedorRepository.findById(id).map(p -> {
            p.setNombreEmpresa(nuevo.getNombreEmpresa());
            p.setRuc(nuevo.getRuc());
            p.setContacto(nuevo.getContacto());
            p.setDireccion(nuevo.getDireccion());
            return proveedorRepository.save(p);
        }).orElseThrow();
    }
}