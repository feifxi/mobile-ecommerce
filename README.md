# 📱  Mobile Shop E-commerce Platform

A full-stack e-commerce web application for mobile devices and accessories, built with Vue.js frontend and Java Spring Boot backend, containerized with Docker for production deployment.

## 🌐 Live Demo

Visit the live application: [https://intproj24.sit.kmutt.ac.th/ms3/](http://intproj24.sit.kmutt.ac.th/ms3/)

## ✨ Features

- **Product Catalog** - Browse mobile phones and accessories
- **Search & Filter** - Find products by brand, price range, and specifications
- **Shopping Cart** - Add/remove items with real-time cart updates
- **User Account** - Registration, login, and profile management
- **Order Management** - Place orders and track order history
- **AI Chatbot** - Integrated chatbot powered by Ollama local LLM
- **Responsive Design** - Optimized for all device sizes

## 🛠️ Technology Stack

### Frontend
- **Vue.js 3** - Progressive JavaScript framework
- **Vue Router** - Client-side routing
- **Pinia** - State management
- **Tailwind CSS** - Styling and responsive design

### Backend
- **Spring Boot (Java)** - Backend service layer
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - ORM for database interaction
- **MySQL** - Relational database
- **MinIO** - Local image storage
- **Ollama** - Local LLM for chatbot feature


### DevOps & Deployment
- **Docker & Docker Compose** - Containerized setup
- **Reverse Proxy (Nginx)** - Frontend/backend routing
- **University VM Server** - Production deployment environment

## 🚀 Getting Started

### Prerequisites

- Node.js (v16+)
- Java JDK 11+
- Maven 3.6+
- Docker & Docker Compose
- MySQL

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/feifxi/MS-3-ITBMS.git
   cd MS-3-ITBMS
   ```

2. **Backend Setup**
   ```bash
   cd itbms-backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **Frontend Setup**
   ```bash
   cd itbms-frontend
   npm install
   npm run dev
   ```

4. **Dev Database, Minio, Ollama etc. Setup via Docker**
   ```docker
   docker compose -f docker-compose-dev.yml up -d
   ```
   
5. **Access the application**
   - Frontend: `http://localhost:5173/ms3/`
   - Backend API: `http://localhost:8080/itb-mshop`

### Docker Deployment

**Build and run with Docker Compose**
   ```bash
   bash start.sh
   ```


## 📁 Project Architecture

```
MS-3-ITBMS/
├── itbms-frontend/          # Vue.js application
│   ├── src/
│   │   ├── components/      # Reusable Vue components
│   │   ├── views/           # Page components
│   │   ├── router/          # Route definitions
│   │   ├── store/           # Pinia store 
├── itbms-backend/           # Spring Boot application
│   ├── src/main/java/
│   │   ├── controller/      # REST controllers
│   │   ├── service/         # Business logic layer
│   │   ├── repository/      # Data access layer
│   │   ├── entity/          # Entity classes
│   │   ├── security/        # Security modules
│   │   └── config/          # Configuration classes
├── itbms-database/          # SQL scripts for init database
├── docker-compose.yml       # Multi-container setup
└── README.md
```

---
⭐ This project was developed as part of INT222 Integrated Project coursework to show the Full Stack skills.