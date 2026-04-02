# Finance Dashboard Backend

A production-ready backend API for a **company finance dashboard system**, built with **Spring Boot**. The system supports role-based access control, financial record management, dashboard analytics, JWT authentication, and more.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Features](#features)
- [Roles & Permissions](#roles--permissions)
- [Getting Started](#getting-started)
- [Environment Configuration](#environment-configuration)
- [Default Admin](#default-admin)
- [API Reference](#api-reference)
- [Assumptions & Design Decisions](#assumptions--design-decisions)
- [Optional Enhancements Implemented](#optional-enhancements-implemented)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.x |
| Language | Java 17+ |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Mapping | ModelMapper |
| Validation | Jakarta Bean Validation |
| Rate Limiting | Bucket4j |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven |
| Utilities | Lombok |

---

## Project Structure

```
src/main/java/com/harsh/FinanceDashboard/Finance/Dashboard/
├── advice/               # Global exception handler, API response wrapper
├── config/               # App config, Swagger config, Data seeder
├── controller/           # REST controllers (thin layer)
├── dto/                  # Request and Response DTOs
├── entities/             # JPA entities
├── enums/                # Role, UserStatus, TransactionType, Category
├── exception/            # Custom exceptions
├── repository/           # JPA repositories with custom JPQL queries
├── security/             # JWT service, auth filter, auth service, security config
└── service/              # Business logic layer
```

---

## Features

### Core Features
- **User & Role Management** — create users, assign roles, manage active/inactive status
- **Financial Records** — full CRUD with soft delete, filtering, pagination and search
- **Dashboard Analytics** — summary, category totals, monthly trends, recent activity
- **Role-Based Access Control** — enforced at the API level using Spring Security and `@PreAuthorize`
- **JWT Authentication** — stateless auth with access token and HttpOnly cookie refresh token
- **Input Validation** — Jakarta Bean Validation with clean error responses
- **Global Exception Handling** — consistent API error responses across all endpoints
- **Soft Delete** — records are marked as deleted, not physically removed
- **Data Seeding** — default admin account created on first startup

### Optional Enhancements
- **Pagination** — all record listing endpoints support page, size and sort
- **Search** — search financial records by keyword (notes or category)
- **Rate Limiting** — 20 requests per minute per IP using Bucket4j
- **Swagger UI** — full interactive API documentation
- **Refresh Token** — stored in HttpOnly cookie for security

---

## Roles & Permissions

| Action | VIEWER | ANALYST | ADMIN |
|---|:---:|:---:|:---:|
| View financial records | ✅ | ✅ | ✅ |
| View dashboard summary | ✅ | ✅ | ✅ |
| View category totals | ✅ | ✅ | ✅ |
| View recent activity | ✅ | ✅ | ✅ |
| Search records | ✅ | ✅ | ✅ |
| View monthly trends | ❌ | ✅ | ✅ |
| Create / Edit / Delete records | ❌ | ❌ | ✅ |
| View deleted records | ❌ | ❌ | ✅ |
| Restore deleted records | ❌ | ❌ | ✅ |
| Manage users (role, status) | ❌ | ❌ | ✅ |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/your-username/finance-dashboard.git
cd finance-dashboard
```

**2. Create PostgreSQL database**
```sql
CREATE DATABASE finance_dashboard;
```

**3. Configure environment** — see [Environment Configuration](#environment-configuration)

**4. Run the application**
```bash
mvn spring-boot:run
```

**5. Access Swagger UI**
```
http://localhost:8080/swagger-ui.html
```

---

## Environment Configuration

Update `src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_dashboard
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secretKey=yourSuperSecretKeyThatIsAtLeast32CharactersLong

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.disable-swagger-default-url=true
springdoc.swagger-ui.url=/api-docs
```

---

## Default Admin

A default admin account is automatically created on first startup:

| Field | Value |
|---|---|
| Email | admin@finance.com |
| Password | admin123 |
| Role | ADMIN |

> **Note:** Change these credentials before deploying to any shared environment.

---

## API Reference

### Authentication — `/api/auth`

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/signup` | Register a new user | Public |
| POST | `/api/auth/login` | Login and get tokens | Public |
| POST | `/api/auth/refresh` | Refresh access token | Public |

**Signup Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Login Request:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Login Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```
> Refresh token is set as an HttpOnly cookie automatically.

---

### User Management — `/api/users`

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/users` | Get all users | ADMIN |
| PATCH | `/api/users/{id}/role` | Update user role | ADMIN |
| PATCH | `/api/users/{id}/status` | Update user status | ADMIN |

**Example — Update Role:**
```
PATCH /api/users/2/role?role=ANALYST
```

**Example — Update Status:**
```
PATCH /api/users/2/status?status=INACTIVE
```

---

### Financial Records — `/api/records`

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/records` | Create a record | ADMIN |
| GET | `/api/records` | Get all records (with filters) | ALL |
| GET | `/api/records/{id}` | Get record by ID | ALL |
| PUT | `/api/records/{id}` | Update a record | ADMIN |
| DELETE | `/api/records/{id}` | Soft delete a record | ADMIN |
| GET | `/api/records/deleted` | Get all deleted records | ADMIN |
| PATCH | `/api/records/{id}/restore` | Restore a deleted record | ADMIN |
| GET | `/api/records/search` | Search records by keyword | ALL |

**Create Record Request:**
```json
{
  "amount": 500000,
  "type": "INCOME",
  "category": "REVENUE",
  "date": "2024-01-15",
  "notes": "Q1 product sales revenue"
}
```

**Filter Records:**
```
GET /api/records?type=INCOME&category=REVENUE&startDate=2024-01-01&endDate=2024-12-31&page=0&size=10
```

**Search Records:**
```
GET /api/records/search?keyword=salary&page=0&size=10
```

**Available Transaction Types:**
- `INCOME`
- `EXPENSE`

**Available Categories:**

| Income | Expense |
|---|---|
| REVENUE | SALARIES |
| SERVICE_INCOME | RENT |
| INVESTMENT_RETURN | UTILITIES |
| LOAN_RECEIVED | OFFICE_SUPPLIES |
| GRANT | MAINTENANCE |
| OTHER_INCOME | MARKETING |
| | TRAVEL |
| | CLIENT_ENTERTAINMENT |
| | SOFTWARE |
| | HARDWARE |
| | TAX |
| | LOAN_REPAYMENT |
| | INSURANCE |
| | AUDIT_LEGAL |
| | RECRUITMENT |
| | TRAINING |
| | EMPLOYEE_BENEFITS |
| | OTHER_EXPENSE |

---

### Dashboard Analytics — `/api/dashboard`

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/dashboard/summary` | Total income, expenses, net balance | ADMIN, ANALYST |
| GET | `/api/dashboard/by-category` | Totals grouped by category | ALL |
| GET | `/api/dashboard/trends` | Monthly income/expense trends | ADMIN, ANALYST |
| GET | `/api/dashboard/recent` | Last 10 transactions | ALL |
| GET | `/api/dashboard/by-category/monthly` | Category totals per month | ALL |

**Summary Response:**
```json
{
  "totalIncome": 1250000.00,
  "totalExpenses": 290000.00,
  "netBalance": 960000.00
}
```

**Monthly Trends Response:**
```json
[
  { "month": 1, "year": 2024, "type": "INCOME", "total": 700000.00 },
  { "month": 1, "year": 2024, "type": "EXPENSE", "total": 175000.00 },
  { "month": 2, "year": 2024, "type": "INCOME", "total": 650000.00 }
]
```

---

## Assumptions & Design Decisions

**Company-wide dashboard** — all authenticated users see the same financial data. This matches the assignment description of a shared finance dashboard, not a personal finance tracker.

**Single role per user** — each user has exactly one role (VIEWER, ANALYST, or ADMIN) rather than multiple roles. This keeps access control simple and clear.

**Soft delete only** — financial records are never permanently deleted. They are flagged with `isDeleted=true` and excluded from all normal queries. Admins can view and restore them.

**Default role is VIEWER** — new signups get the VIEWER role. An Admin must explicitly promote users to ANALYST or ADMIN.

**JWT is stateless** — no sessions are stored server-side. The access token carries the user's ID and role. The refresh token is stored in an HttpOnly cookie for security.

**Admin cannot be deactivated** — the system prevents deactivating an Admin account to avoid lockout scenarios.

**Category is an enum** — categories are predefined to ensure data consistency and prevent typos in financial records.

**PostgreSQL-compatible queries** — dashboard aggregation queries use `EXTRACT(MONTH FROM date)` instead of MySQL's `MONTH()` function.

---

## Optional Enhancements Implemented

| Enhancement | Details |
|---|---|
| JWT Authentication | Access token (10 min) + Refresh token (6 months) via HttpOnly cookie |
| Pagination | All record listing endpoints support `page`, `size`, and `sort` |
| Search | Keyword search across record notes and categories |
| Soft Delete | Records flagged with `isDeleted`, restorable by Admin |
| Rate Limiting | 20 requests/minute per IP via Bucket4j |
| API Documentation | Full Swagger UI at `/swagger-ui.html` |

---

## Swagger UI

Once the application is running, visit:

```
http://localhost:8080/swagger-ui.html
```

To test authenticated endpoints:
1. Call `POST /api/auth/login` via Try it out
2. Copy the `accessToken` from the response
3. Click **Authorize** at the top right
4. Paste the token and click **Authorize**
5. All secured endpoints are now accessible

---

## Author

**Harsh Choudhary**
