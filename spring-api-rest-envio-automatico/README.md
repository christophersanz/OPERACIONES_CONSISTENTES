# Spring API REST para Envío Automático de Registros

Este proyecto implementa una API REST en Spring Boot para el envío automático de registros a través de HTTP. La API recupera registros de una base de datos y los envía a otra API o servicio de manera incremental.

## Funcionalidades

- Envío automático incremental de registros.
- Implementación de paginación para manejar grandes volúmenes de datos.
- Integración con otras APIs mediante solicitudes HTTP.

## Configuración y Uso

1. **Clonar el repositorio:**
-  git clone https://github.com/tu-usuario/spring-api-rest-envio-automatico.git


2. **Configurar la base de datos:**
- Configura las propiedades de conexión a la base de datos en `application.properties`.

3. **Ejecutar la aplicación:**
-  mvn spring-boot:run

4. **Consumir la API:**
- La API estará disponible en `http://localhost:9080`.

## Dependencias

- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Scheduled
- RestTemplate

## Contribución

Si deseas contribuir a este proyecto, por favor sigue estos pasos:

1. Haz un fork del proyecto.
2. Crea una nueva rama para tu funcionalidad (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza tus cambios y haz commit de ellos (`git commit -am 'Agrega nueva funcionalidad'`).
4. Haz push de tu rama al repositorio remoto (`git push origin feature/nueva-funcionalidad`).
5. Crea un nuevo Pull Request.

## Autores

- Nombre del Autor 1 - [@usuario1](https://github.com/usuario1)
- Nombre del Autor 2 - [@usuario2](https://github.com/usuario2)

## Licencia

Este proyecto está bajo la [Licencia MIT](LICENSE).
