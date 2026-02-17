## Ecom Project (Spring Boot + React)

Full‑stack eCommerce **product catalog** with **CRUD**, **image upload**, **search/filter**, and a **cart + checkout modal** UI.

## Highlights (what this project demonstrates)

- **Backend API**: Spring Boot (Java 21), REST endpoints, Spring MVC + Spring Data JPA, MySQL persistence
- **Frontend**: React 18 + Vite, React Router, Context API state, Axios, Bootstrap UI
- **Real-world features**: product listing + details, search, category filtering, admin-style add/update/delete, cart persisted in `localStorage`

## Features

- **Product catalog**: list products, view product details
- **Search**: keyword search across name/description/brand/category
- **Categories**: filter products by category from navbar dropdown
- **Product images**: upload an image per product; image served by API and rendered in UI
- **Admin actions**: add/update/delete product (UI screens)
- **Cart**: add/remove items; checkout confirmation modal
- **Theme toggle**: light/dark theme saved in browser storage

## Tech stack

- **Backend**: Spring Boot `4.0.1`, Java `21`, Spring WebMVC, Spring Data JPA, Lombok, MySQL
- **Frontend**: React `18`, Vite `5`, React Router, Axios, Bootstrap + Bootstrap Icons
- **Build/Tooling**: Maven Wrapper (`mvnw.cmd`), ESLint

## Project structure (high level)

- **Backend (Spring Boot)**: `src/main/java/in/lifehive/ecom_proj/`
  - `controller/` REST endpoints
  - `service/` business logic
  - `repository/` JPA repositories + queries
  - `model/` entities
- **Frontend (React)**: `frontend/ecom-frontend/`
  - `src/components/` UI screens/components
  - `src/Context/Context.jsx` app state (products + cart)

## Quickstart (local)

### Prerequisites

- Java **21**
- Node.js (LTS recommended)
- MySQL running locally

### 1) Database

Create a database named `ecommerce` (or update the JDBC URL in `src/main/resources/application.properties`).

### 2) Run backend (port 8080)

From repo root:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell you can also use:

```bash
.\mvnw.cmd spring-boot:run
```

### 3) Run frontend (port 5173)

```bash
cd frontend/ecom-frontend
npm install
npm run dev
```

## API overview

Base URL: `http://localhost:8080/api`

- `GET /products` — list all products
- `GET /product/{prodId}` — get product by id
- `GET /product/{prodId}/image` — get product image bytes
- `POST /product` — create product (**multipart**: `product` JSON + `imageFile`)
- `PUT /product/{prodId}` — update product (**multipart**: `product` JSON + `imageFile`)
- `DELETE /product/{prodId}` — delete product
- `GET /products/search?keyword=...` — search products

Example (search):

```bash
curl "http://localhost:8080/api/products/search?keyword=laptop"
```

## Notes

- **CORS** is configured to allow the Vite dev server (`http://localhost:5173`).
- Product images are currently stored in the database as a **BLOB** (`imageData` on `Product`).

## For maintainers

If you’re picking this project back up later, see **`PROJECT_GUIDE.md`** (setup checklist, architecture map, and known quirks).
