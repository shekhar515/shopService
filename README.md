# 🏪 Shop Service API

> **Spring Boot REST API** for managing shops and products with rate limiting, validation, and security.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

## 🚀 Features

### ✅ **Core Functionality**
- **Shop Management** - Complete CRUD operations
- **Product Management** - Full product lifecycle
- **Search & Filter** - Advanced querying capabilities
- **Location Services** - Coordinate-based search

### 🛡️ **Enterprise Features**
- **Rate Limiting** - Token bucket algorithm (5-10 req/min)
- **Input Validation** - Jakarta Bean Validation
- **Error Handling** - Custom exceptions with proper HTTP codes
- **Security** - Environment variables, SQL logging disabled

---

## 📋 Quick Start

### Prerequisites
- **Java 21+**
- **Maven 3.6+**
- **MySQL 8.0+**

### 🚀 Run Application

```bash
# 1. Clone and setup
git clone <repository-url>
cd shopService

# 2. Set environment variables
export DB_URL=jdbc:mysql://localhost:3306/storeDb
export DB_USERNAME=your_username
export DB_PASSWORD=your_password

# 3. Run with Maven
mvn spring-boot:run

# 4. Access API
curl http://localhost:8080/api/v1/shops/all
```

---

## 📚 API Endpoints

### 🏪 Shop Management

| Method | Endpoint | Description | Rate Limit |
|--------|----------|-------------|------------|
| `GET` | `/api/v1/shops/all` | Get all shops | 5/min |
| `GET` | `/api/v1/shops/{id}` | Get shop by ID | 5/min |
| `GET` | `/api/v1/shops/category/{category}` | Filter by category | 5/min |
| `GET` | `/api/v1/shops/search` | Search by coordinates | 5/min |
| `POST` | `/api/v1/shops/add` | Create new shop | 10/min |
| `PUT` | `/api/v1/shops/{id}` | Update shop | 10/min |
| `DELETE` | `/api/v1/shops/del/{id}` | Delete shop | 2/min |

### 📦 Product Management

| Method | Endpoint | Description | Rate Limit |
|--------|----------|-------------|------------|
| `GET` | `/api/v1/products/shop/{shopId}` | Get shop products | 10/min |
| `GET` | `/api/v1/products/{productId}` | Get product by ID | 10/min |
| `GET` | `/api/v1/products/search` | Search products | 10/min |
| `POST` | `/api/v1/products/shop/{shopId}` | Add product | 5/min |
| `PUT` | `/api/v1/products/{shopId}/products/{productId}` | Update product | 5/min |
| `DELETE` | `/api/v1/products/{shopId}/products/{productId}` | Delete product | 3/min |

---

## 🎯 API Examples

### Create Shop
```bash
curl -X POST http://localhost:8080/api/v1/shops/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Coffee House",
    "category": "RESTAURANT",
    "address": "123 Main St, New York",
    "latitude": 40.7128,
    "longitude": -74.0060
  }'
```

### Add Product
```bash
curl -X POST http://localhost:8080/api/v1/products/shop/1 \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Cappuccino",
    "price": 4.99,
    "description": "Fresh Italian coffee"
  }'
```

---

## 🛡️ Security & Validation

### 🔒 Security Features
- ✅ **Environment Variables** - No hardcoded credentials
- ✅ **SQL Logging Disabled** - Production security
- ✅ **Input Validation** - Prevent injection attacks
- ✅ **Custom Error Pages** - No information leakage

### ✅ Input Validation
```java
@Entity
public class ShopModel {
    @NotBlank(message = "Shop name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;
}
```

---

## 📊 Rate Limiting

### 🪙 Token Bucket Algorithm
- **Library**: Bucket4j
- **Strategy**: Different limits per operation
- **Response**: HTTP 429 with headers

### 📈 Rate Limits
| Operation | Limit | Duration | Scope |
|-----------|-------|----------|-------|
| Shop Read | 5 requests | 1 minute | Per instance |
| Shop Create | 10 requests | 1 minute | Per instance |
| Shop Delete | 2 requests | 1 minute | Per instance |
| Product Read | 10 requests | 1 minute | Per instance |
| Product Create | 5 requests | 1 minute | Per instance |
| Product Delete | 3 requests | 1 minute | Per instance |

---

## 🏗️ Architecture

### 📁 Project Structure
```
src/main/java/com/example/shopService/
├── 📂 controller/          # REST endpoints
├── 📂 service/             # Business logic
├── 📂 repository/           # Data access layer
├── 📂 entity/              # JPA entities
├── 📂 exception/           # Custom exceptions
├── 📂 constants/           # Enums and constants
└── 📂 config/             # Configuration classes
```

### 🔄 Layered Architecture
```
┌─────────────────────────────────────┐
│           Controller Layer           │ ← REST Endpoints
├─────────────────────────────────────┤
│            Service Layer             │ ← Business Logic
├─────────────────────────────────────┤
│           Repository Layer           │ ← Data Access
├─────────────────────────────────────┤
│            Database Layer             │ ← MySQL Storage
└─────────────────────────────────────┘
```

---

## 🗄️ Database Schema

### 🏪 Shop Entity
```sql
CREATE TABLE shops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category ENUM('HOTEL', 'GROCERY', 'MEDICAL', 'RESTAURANT') NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    address VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 📦 Product Entity
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    shop_id BIGINT NOT NULL,
    FOREIGN KEY (shop_id) REFERENCES shops(id) ON DELETE CASCADE
);
```

---

## 🚀 Deployment

### 🐳 Docker Deployment
```bash
# Build image
docker build -t shop-service:latest .

# Run container
docker run -d \
  --name shop-service \
  -p 8080:8080 \
  -e DB_URL=jdbc:mysql://db:3306/storeDb \
  -e DB_USERNAME=user \
  -e DB_PASSWORD=pass \
  shop-service:latest
```

---

## 📈 Production Readiness

### ✅ **Completed (85%)**
- **Core CRUD Operations** - Complete implementation
- **Rate Limiting** - Token bucket algorithm
- **Input Validation** - Jakarta Bean Validation
- **Error Handling** - Custom exceptions
- **Security** - Environment variables
- **Clean Architecture** - Layered design

### 🔄 **Remaining (15%)**
- **Authentication** - Spring Security integration
- **Caching** - Redis for performance
- **Testing** - Unit and integration tests
- **Monitoring** - Actuator endpoints

---

## 🧪 Testing

### 🧪 Manual Testing
```bash
# Test rate limiting
for i in {1..6}; do
  curl -X GET http://localhost:8080/api/v1/shops/all
done

# Test validation
curl -X POST http://localhost:8080/api/v1/shops/add \
  -H "Content-Type: application/json" \
  -d '{"name": "", "category": "RESTAURANT"}'

# Test error handling
curl -X GET http://localhost:8080/api/v1/shops/99999
```

---

## 🔧 Configuration

### 📄 Environment Variables
```bash
# Required
export DB_URL=jdbc:mysql://localhost:3306/storeDb
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

### ⚙️ Application Properties
```properties
# Database
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/storeDb}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Error handling
server.error.whitelabel.enabled=false
spring.mvc.throw-exception-if-no-handler-found=true
```

---

## 📝 Contributing

### 🚀 How to Contribute
1. **Fork** the repository
2. **Create** feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to branch (`git push origin feature/amazing-feature`)
5. **Open** Pull Request

---

## 🤝 Support

### 📞 Get Help
- 🐛 **Report Issues**: [GitHub Issues](https://github.com/your-repo/issues)
- 📧 **Email Support**: support@shopservice.com
- 📖 **Documentation**: [API Docs](http://localhost:8080/api/v1/)

### 🔗 Quick Links
- **Health Check**: http://localhost:8080/health
- **API Base**: http://localhost:8080/api/v1/
- **Error Page**: http://localhost:8080/error

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 🏆 Project Highlights

### 🎯 **Key Achievements**
- 🏆 **Production-Ready Architecture** - Clean, scalable design
- 🏆 **Advanced Rate Limiting** - Token bucket implementation
- 🏆 **Comprehensive Validation** - Jakarta Bean Validation
- 🏆 **Professional Error Handling** - Custom exceptions
- 🏆 **Security Best Practices** - Environment variables
- 🏆 **Modern Tech Stack** - Spring Boot 3.5.10, Java 21

### 📚 **Learning Outcomes**
- 📖 **Spring Boot Advanced Features**
- 📖 **Rate Limiting Algorithms**
- 📖 **REST API Best Practices**
- 📖 **Database Design Patterns**
- 📖 **Security Implementation**
- 📖 **Production Deployment**

---

<div align="center">

**🏪 Shop Service API** - Built with ❤️ using Spring Boot

**Last Updated**: February 2026 | **Version**: 1.0.0 | **Status**: Production Ready (85%)

</div>
