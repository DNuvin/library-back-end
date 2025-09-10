# Library Management System API

This repository contains the **Library Management System** backend API, a simple system to manage books, borrowers, and borrowing transactions. The API is built using **Java 17** and **Spring Boot**, following clean coding practices and a modular structure suitable for scalable backend applications.

---

## Project Description

The Library Management System API allows users to:

- Manage borrowers
- Manage books
- Track which books are borrowed and returned

This project is deployed on **AWS EC2** with **Kubernetes (K3s)** and uses **Jenkins** for CI/CD pipelines. The system is **environment-agnostic**, with configuration managed via **ConfigMaps** and **Secrets**.

---

## Features

### Borrower Management

- Register a new borrower

### Book Management

- Register a new book
- Get a list of all books in the library

### Borrowing Management

- Borrow a book (only if it is available)
- Return a borrowed book

---

## Deployment & Environment

- **AWS EC2 Public IP:** `47.130.167.47`
- **Library app NodePort:** `32000`
- **Jenkins URL:** [http://47.130.167.47:8080](http://47.130.167.47:8080)
  - Username: `test_user`
  - Password: `test123`
  - Pipeline: `library-pipeline-dev`
- **Docker Repository:** [https://hub.docker.com/r/dewuruge/library/tags](https://hub.docker.com/r/dewuruge/library/tags)

---

## Access URLs

- **Swagger UI:** [http://47.130.167.47:32000/library-service/swagger](http://47.130.167.47:32000/library-service/swagger)
- **OpenAPI Docs:** [http://47.130.167.47:32000/library-service/v3/api-docs](http://47.130.167.47:32000/library-service/v3/api-docs)
- **Test Coverage Report:** [http://47.130.167.47:32000/library-service/coverage](http://47.130.167.47:32000/library-service/coverage)

---

## Tech Stack

- **Java 17**
- **Spring Boot (REST API)**
- **PostgreSQL**
- **Docker & Kubernetes (K3s)**
- **Jenkins (CI/CD)**
- **Git & GitHub**

---

## Notes

- Environment configurations are managed using **ConfigMaps** and **Secrets**, enabling environment-agnostic deployments.
- Book availability is tracked: a book cannot be borrowed by multiple users at the same time.
- **This setup is only for demonstration purposes**:
  - Test coverage report exists but is **not fully covered**
  - Only the `dev` environment is created under the `dev` namespace
  - Only **one Jenkins pipeline** is configured to build and deploy the application
  - The **`dev` branch** in GitHub triggers the **`library-pipeline-dev`** Jenkins pipeline

---

## Local Development & Running Locally

This local setup will:

- Build the Library Service image (including compiling the Java project)
- Start PostgreSQL on `localhost:5432`
- Start Library Service on `http://localhost:8080/library-service`
- Automatically wait for PostgreSQL to be healthy before starting the app

### Prerequisites

Make sure you have installed:

- [Docker & Docker Compose](https://docs.docker.com/compose/install/)
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
- [Maven](https://maven.apache.org/install.html)

---

### Steps to Run Locally

#### 1. Clone the repository
```bash
git clone https://github.com/DNuvin/library-back-end.git
cd library-back-end
```

#### 2. Build and start the application stack
```bash
docker-compose up --build
```

#### 3. Verify services are running 
```bash
docker ps
```

You should see two containers running:
- postgres
- library-service
#### 3. Verify services are running
```bash
docker ps
```

#### 4. Run in detached mode (optional)
```bash
docker-compose up -d --build
```

#### 5. Monitor logs
```bash
docker-compose logs -f
```
## Access URLs (Local)

- **Swagger UI:** [http://localhost:8080/library-service/swagger](http://localhost:8080/library-service/swagger)
- **OpenAPI Docs:** [http://localhost:8080/library-service/v3/api-docs](http://localhost:8080/library-service/v3/api-docs)
- **Test Coverage Report:** [http://localhost:8080/library-service/coverage](http://localhost:8080/library-service/coverage)

---

#### 6. Stop services
```bash
docker-compose down
```
