# Guía de Contribución y Estándares - Zarpronix

Para asegurar la calidad del proyecto de Ingeniería de Software, todo el equipo debe seguir estas normas:

## 1. Nomenclatura de Código
- **Java:** Usar `PascalCase` para clases e interfaces, `camelCase` para métodos y variables, y `SCREAMING_SNAKE_CASE` para constantes.
- **Base de Datos:** Usar `snake_case` para tablas y columnas. Las tablas del módulo de auditoría deben llevar el prefijo `tbl_`.

## 2. Gestión de Versiones (Commits)
Se utilizará el formato **Conventional Commits**:
- `feat(módulo): descripción` (Para nuevas funciones).
- `fix: descripción` (Para corregir errores).
- `docs: descripción` (Para cambios en documentación).

## 3. Documentación y Estilo
- **Comentarios:** Uso obligatorio de **JavaDoc** en todos los métodos de la capa `Service`. Los comentarios *inline* se usarán solo para lógica compleja.
- **Formato:** Se sigue la **Google Java Style Guide** con una indentación de 4 espacios, verificada mediante Checkstyle.
