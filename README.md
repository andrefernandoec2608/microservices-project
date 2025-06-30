# 🧩 Microservices System with Spring Boot, Kafka, Docker & PostgreSQL

Este proyecto implementa una arquitectura de microservicios moderna que integra **Spring Boot**, **Apache Kafka**, **Docker** y **PostgreSQL**, mostrando prácticas recomendadas en diseño backend, comunicación asincrónica y manejo de excepciones.

## 🎯 Propósito

El objetivo principal es demostrar:

- Una **arquitectura de microservicios** real, distribuida y escalable.
- Comunicación asincrónica con **Kafka**.
- Manejo de excepciones controladas.
- Contenerización de cada servicio con **Docker**.
- Independencia de persistencia con **bases de datos separadas** por servicio.

---

## 🛠️ Tecnologías utilizadas

- Spring Boot
- Apache Kafka
- Spring Data JPA
- Spring Cloud Gateway
- PostgreSQL
- Docker & Docker Compose

---

## 🏗️ Arquitectura General

```
                   [API GATEWAY] 
                                |
               -----------------------------------
               |                                 |
       [cliente-service]                 [cuenta-service]
         (clientes/personas)              (cuentas/movimientos)
               |   🔽                             ▲
               |   🔊 Kafka Topic: clientes       |
               -----------------------------------
                          Comunicación asincrónica
```

---

## 🧩 Microservicios

### ✅ cliente-service
- Expone API REST para:
  - Crear clientes
  - Consultar clientes
- Publica eventos Kafka cuando se crea un cliente nuevo.
- Base de datos independiente: **PostgreSQL (`clientes_db`)**

---

### ✅ cuenta-service
- Escucha eventos Kafka de `cliente-service` para crear cuentas automáticamente.
- Expone API REST para:
  - Consultar y crear cuentas
  - Registrar movimientos
- Base de datos independiente: **PostgreSQL (`cuentas_db`)**

---

### ✅ gateway-service
- Punto único de entrada al sistema.
- Implementado con **Spring Cloud Gateway**.
- Redirige automáticamente:
  - `/api/clients/**` ➡️ `cliente-service`
  - `/api/accounts/**` ➡️ `cuenta-service`
- Expone endpoints de composición:
  - Consolidación de datos cliente/cuenta.
  - Listado de movimientos ordenados por fecha.

---

## 🐳 Docker

Cada microservicio está contenerizado en su propio **Docker container**.  
Se recomienda usar **Docker Compose** para levantar el ecosistema completo con Kafka y PostgreSQL.

---

## 🧪 Pruebas

Incluye:
- Pruebas unitarias.
- Pruebas de integración.
- Validaciones de flujo de eventos Kafka.

---

## 👨‍💻 Autor
[![LinkedIn](https://img.shields.io/badge/LinkedIn-André%20Llumiquinga-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/andre-llc/)
[![GitHub](https://img.shields.io/badge/GitHub-André%20Llumiquinga-black?style=flat&logo=github)](https://github.com/andrefernandoec2608)
