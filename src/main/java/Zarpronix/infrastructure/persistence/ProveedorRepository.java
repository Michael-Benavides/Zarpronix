package Zarpronix.infrastructure.persistence;

import Zarpronix.domain.model.Proveedor; // <--- Import clave
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}