# 🧪 Microservices Postman Collection

Este repositorio contiene una colección de **Postman** diseñada para probar la funcionalidad de los microservicios desarrollados en Spring Boot y Kafka.

La colección se encuentra en el archivo:

```
microservices-project.postman_collection.json
```

---

## 📁 Estructura de la colección

La colección tiene **2 carpetas principales**, cada una con solicitudes específicas:

---

### ✅ cliente-service

#### 🟢 ClientCreation
- **Método:** POST
- **Descripción:** Crea un cliente nuevo.
- **Body:** Contiene un JSON de ejemplo con los datos del cliente.

#### 🟢 ClientsReport
- **Método:** GET
- **Descripción:** Lista todos los clientes registrados.
- **Parámetros:** Ninguno.

---

### ✅ cuenta-service

#### 🟢 AccountCreation
- **Método:** POST
- **Descripción:** Crea una cuenta bancaria.
- **Body:** Incluye un JSON de ejemplo.

---

#### 🟢 MovementCreation
- **Método:** POST
- **Descripción:** Registra depósitos o retiros en una cuenta existente.
- **URL de ejemplo:**
  ```
  http://localhost:8082/api/account/number/{número de cuenta}/movements
  ```

---

#### 🟢 AccountsByUserReport
- **Método:** GET
- **Descripción:** Lista todas las cuentas de un usuario específico.
- **URL de ejemplo:**
  ```
  http://localhost:8082/api/account_report/{id del usuario}
  ```

---

#### 🟢 StatementByAccountReport
- **Método:** GET
- **Descripción:** Devuelve el estado de cuenta de una cuenta.
- **URL de ejemplo:**
  ```
  http://localhost:8082/api/movement_report/{número de cuenta}
  ```

---

## 🚀 Cómo importar la colección

1. Abre **Postman**.
2. Ve a **File > Import**.
3. Selecciona el archivo:
   ```
   microservices-project.postman_collection.json
   ```
4. La colección aparecerá con todas las carpetas y endpoints listos para probar.

---

## 📝 Notas

- Verifica que los microservicios estén en ejecución antes de hacer las peticiones.
- Puedes ajustar las variables de entorno (p. ej., `localhost`, puertos) según tu configuración.

---

## 👨‍💻 Autor
[![LinkedIn](https://img.shields.io/badge/LinkedIn-André%20Llumiquinga-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/andre-llc/)
[![GitHub](https://img.shields.io/badge/GitHub-André%20Llumiquinga-black?style=flat&logo=github)](https://github.com/andrefernandoec2608)
