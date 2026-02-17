## Project Guide (restart this in 6 months)

This file is written for “future me” so I can quickly understand **what works**, **where things live**, and **how to extend it**.

## 0) What this project is

Full-stack eCommerce **product catalog + cart**:

- Spring Boot REST API + MySQL (products + image upload)
- React (Vite) UI with product listing/detail, search, category filter, cart + checkout modal

## 1) Run it locally (fast checklist)

### Backend (Spring Boot)

- **Prereqs**: Java 21, MySQL running
- **Config**: `src/main/resources/application.properties`
  - JDBC URL targets db `ecommerce`
  - Uses `spring.jpa.hibernate.ddl-auto=update` to create/update tables automatically

Run:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
.\mvnw.cmd spring-boot:run
```

Backend runs at: `http://localhost:8080`

### Frontend (React)

```bash
cd frontend/ecom-frontend
npm install
npm run dev
```

Frontend runs at: `http://localhost:5173`

## 2) Where the code is (map)

### Backend

Root package: `src/main/java/in/lifehive/ecom_proj/`

- **Entry point**: `EcomProjApplication.java`
- **CORS**: `WebConfig.java` (allows `http://localhost:5173`)
- **Product domain**:
  - `model/Product.java`
  - `repository/ProductRepository.java`
  - `service/ProductService.java`
  - `controller/ProductController.java`

### Frontend

`frontend/ecom-frontend/src/`

- **API helper**: `axios.jsx` (Axios instance with baseURL `http://localhost:8080/api`)
- **State**: `Context/Context.jsx`
  - Fetches products via `GET /products`
  - Stores cart in `localStorage`
- **Routes**: `App.jsx`
  - `/` Home (grid)
  - `/add_product` add product form
  - `/product/:id` product detail
  - `/product/update/:id` update form
  - `/cart` cart + checkout modal
- **Key components**: `components/`
  - `Home.jsx`, `Product.jsx`, `AddProduct.jsx`, `UpdateProduct.jsx`, `Cart.jsx`, `CheckoutPopup.jsx`, `Navbar.jsx`

## 3) Backend API details

Base path: `/api` (see `ProductController`)

- **List all products**
  - `GET /api/products`
  - Returns `404` if empty (current behavior)
- **Get one product**
  - `GET /api/product/{prodId}`
- **Get image bytes**
  - `GET /api/product/{prodId}/image`
  - Returns raw bytes; frontend requests as `blob`
- **Create product (multipart)**
  - `POST /api/product`
  - Parts:
    - `product`: JSON (as a multipart part)
    - `imageFile`: file
- **Update product (multipart)**
  - `PUT /api/product/{prodId}`
  - Same parts as create
- **Delete product**
  - `DELETE /api/product/{prodId}`
- **Search**
  - `GET /api/products/search?keyword=...`
  - Query defined in `ProductRepository.searchProducts`

### Data model: `Product`

Entity: `Product.java`

- Core fields: `name`, `description`, `brand`, `price`, `category`, `releaseDate`, `available`, `quantity`
- Audit: `createdAt`, `updatedAt`
- Image: `imageName`, `imageType`, `imageData` (`@Lob byte[]`)

## 4) Frontend behavior (important flows)

### Product listing (`Home.jsx`)

- Loads products from Context (`refreshData()` -> `GET /products`)
- For each product, calls `GET /product/{id}/image` and renders the blob
- Supports **category filtering** (driven by navbar selection)

### Search (`Navbar.jsx`)

- Calls `GET /products/search?keyword=...`
- Shows dropdown results linking to `/product/{id}`

### Add Product (`AddProduct.jsx`)

- Builds `FormData`:
  - `imageFile` = selected image
  - `product` = JSON blob
- Sends `POST http://localhost:8080/api/product`

### Update/Delete (`Product.jsx` + `UpdateProduct.jsx`)

- Detail page can:
  - Navigate to update screen
  - Delete product (`DELETE /product/{id}`)
- Update screen:
  - Fetches existing product + image
  - Sends multipart `PUT /product/{id}`

### Cart + checkout (`Context.jsx`, `Cart.jsx`)

- Cart stored in `localStorage`
- Checkout shows modal with line items and totals

## 5) Known quirks / tech debt (good to remember later)

- **Hardcoded API URLs in multiple components**:
  - Some components use `axios` directly with full `http://localhost:8080/...` instead of the shared `src/axios.jsx` instance.
  - If you deploy, you’ll want a single configurable API base (env var) and consistent usage.
- **Credentials in `application.properties`**:
  - Currently contains a username/password. For a public repo, move secrets to a local-only file or environment variables.
- **`GET /products` returns 404 when empty**:
  - Many APIs return `200 []`. Consider changing if you want more standard behavior.
- **Cart stock/checkout logic likely needs review**:
  - If you revisit checkout/inventory updates, re-check the quantity calculations and update payload.

## 6) Common changes (how to extend)

### Add a new backend endpoint

- Add method in `controller/*Controller.java`
- Add logic in `service/*Service.java`
- Add repo method/query in `repository/*Repository.java` if needed
- Test quickly with curl or Postman

### Add a new frontend page

- Create component in `src/components/`
- Add route in `src/App.jsx`
- Use Context or create a small API module to call backend

### Change database schema

- Update entity in `model/`
- With `ddl-auto=update`, schema will evolve automatically (OK for dev; not ideal for prod migrations)

## 7) Quick troubleshooting

- **Frontend can’t call backend (CORS)**:
  - Check `WebConfig.java` and ensure frontend URL matches (`http://localhost:5173`)
- **MySQL connection fails**:
  - Verify db exists (`ecommerce`), user/password, and MySQL is running
- **Images not showing**:
  - Verify `GET /api/product/{id}/image` works and the frontend request is `responseType: "blob"`

## 8) Next improvements (nice roadmap)

- Authentication + roles (admin vs customer)
- Proper Orders/Checkout backend (order tables, payments)
- Move images to object storage (or filesystem) instead of DB BLOB
- Validation + error responses (standard API response wrapper)
- Pagination/sorting for product list

