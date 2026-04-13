package Zarpronix.infrastructure.persistence;

import Zarpronix.domain.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
    // Esto permite buscar, guardar y borrar productos automáticamente
}