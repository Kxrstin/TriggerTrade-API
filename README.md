# TriggerTrade API
## Backend system for e-commerce with automated warehouse management, JDBC & SQL logic.
TriggerTrade API is an efficient backend system for managing shop data.
The system manages customers, employees, products, and orders.
What makes it special:
Instead of relying on complex framework abstractions, this project uses
**pure JDBC** for maximum control over database access.
The business logic for inventory checking is anchored directly in PostgreSQL triggers,
which makes the system particularly performant and consistent.

--- 

## Get started
Follow these steps to start the project in your local environment.

### 1. Requirements
* **Java 17** (JDK)
* **Docker & Docker Desktop**
* **Maven**

### 2. Start Docker
Start the PostgreSQL database, including all triggers and views, via Docker Compose:
```bash
docker-compose up -d
```

### 3. Start application
Start the Spring Boot application:

```bash
cd spring-boot/
mvn clean install
mvn spring-boot:run
```

Once the process is complete, the API can be accessed at:

```bash
http://localhost:8080
```

You can also use Swagger-ui
```bash
http://localhost:8080/swagger-ui/index.html
```

---

## Demo & Screenshots

<table>
<tr>
<td align="center"><b>Database Design</b></td>
<tr>
<td><img src="screenshots/db-schema.png" width="700px" alt="Database Schema"></td>
</tr>
<tr>
<td align="center"><b>Small selection of available resources</b></td>
</tr>
<tr>
<td><img src="screenshots/swagger.png" width="700px" alt="Small selection of available resources"></td>
</tr>
</table>

---

## Features

### Business Features

* **Customer & Address Management:** Complex relationships between customers and multiple address types (shipping/billing address) including validation.
* **Employee Portal:** Overview of managed products and order status.
* **Product Catalog:** Management of SKUs, prices, and real-time inventory.
* **Ordering System:** Complete process from order to cancellation with automatic inventory reconciliation.
* **Real-time inventory:** Automated inventory management for every sale or cancellation.
* **Order history:** Traceability of orders across different status stages.

### Technical Highlights

* **RESTful design:** Consistent use of HTTP methods (GET, POST, PUT, DELETE) and appropriate status codes.
* **Direct JDBC Access:** No ORM layer (such as Hibernate) in favor of native SQL for full transparency and performance.
* **PL/pgSQL Trigger:** Automated validation of inventory directly during INSERT/UPDATE on order items—prevents stock shortages through automated trigger functions.
* **Robust SQL Schema:** Complex regex constraints for passwords, emails, and zip codes directly in the database.
* **Analytical Views:** Pre-calculated SQL views for sales statistics and employee KPIs.

---

## Tech Stack

### Backend

* **Java 17** & **Spring Boot**
* **JDBC** (Native SQL Queries)
* **Maven** (Build Management)

### Database

* PostgreSQL (Relationales Modell)
* PL/pgSQL (Stored Procedures & Trigger)

### Infrastructure

* **Docker** & **Docker Compose**

---

### What I learned

* **Native SQL over ORM:** I learned how to control database access directly with JDBC. This gave me a deeper understanding of ResultSets, PreparedStatements, and the manual mapping of SQL data to Java objects.
* **Database Intelligence:** Instead of writing logic only in Java code, I learned to use PL/pgSQL triggers. This prevents inconsistencies (e.g., “overselling”) even when manipulating data directly via the SQL console.
* **REST semantics:** I learned how to structure an API so that it is intuitive to use (e.g., using path variables for IDs and query parameters for filters).
* **Relational design:** Creating complex join tables (such as `customer_has_addresses`) deepened my understanding of m:n relationships and foreign key constraints.
* **Regex & Data Integrity:** Implementing complex CHECK constraints in SQL showed me how to use a “defense in depth” strategy to reject invalid data at the lowest level.
* **Docker Orchestration:** Through Docker Compose, I learned how to automate the provisioning of complex dependencies (such as the initial execution of schema and data scripts).

---

### 🔒 Security Note

For demonstration purposes, this project uses standard credentials in the Docker setup. In a production environment, this would be replaced by environment variables and secret management.

---

### 🎓 Background: University Project

This project was developed as part of the **“Databases: Advanced Concepts”** module in the 5th semester.

The focus was on:

* **Practical application:** Bridging the gap between complex SQL (triggers/procedures) and modern Java backend development.
* **Integrity:** Proving that critical business logic (such as inventory checks) can be securely encapsulated at the database level.
* **Full-stack thinking:** Providing a containerized environment that is ready for immediate use without manual configuration.