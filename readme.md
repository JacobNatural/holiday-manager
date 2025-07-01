
# Holiday Manager Application

![Static Badge](https://img.shields.io/badge/Build-passing-flat)
[![Static Badge](https://img.shields.io/badge/docs-blue)](https://jacobnatural.github.io/holiday-manager/index.html)
## Backend Overview
Holiday Manager is a secure, scalable, and efficient backend solution built with Spring Boot,
designed to streamline the management of employee vacation requests. This system provides a
clean and structured way to handle vacation workflows, offering role-based access, robust validation,
and modern authentication mechanisms.

The application features two user roles — ADMIN, who oversees and manages all vacation requests,
and WORKER, who can create and track their own requests. Leveraging Spring Security and
JWT token-based authentication, Holiday Manager ensures safe and stateless access control,
aligning with modern REST API standards.

Built with maintainability in mind, the application follows best practices in backend development,
including layered architecture, clear separation of concerns, git clone comprehensive validation rules to
prevent invalid or conflicting vacation dates.

Holiday Manager is containerized using Docker, making deployment seamless across environments.
The system exposes a set of well-documented RESTful endpoints, easily explorable through Swagger UI,
facilitating smooth integration with frontend or third-party services

## Frontend Overview
The frontend is built with React, using a custom router combined with React Context API
to manage authentication state throughout the application. Routes are protected by a custom
<ProtectedRoute> component that restricts access based on user authentication.

Key pages include:

Public pages such as Home, Login, Register, Activation, Forgot Password, and New Password.

Worker-specific pages like the Worker Dashboard, Holidays overview, Settings, Password and Email changes.

Admin-specific pages including the Admin Dashboard, Holiday Manager, and User Manager.

This structure ensures a smooth user experience by conditionally rendering routes based on user roles and
authentication status, maintaining security and usability seamlessly on the client side.

## Technology Stack

- Backend: Java, Spring Boot, Spring Security JWT

- Frontend: React (hooks, Context API, custom routing), fetch API

- Database: (MySQL)

- Build Tool: Maven

- Other Tools: Hibernate, Lombok

- Containerization: Docker

## Getting Started

### Prerequisites
- **Docker**

### Cloning the repository:
- To clone the repository and navigate into the project directory, run:

```bash
git clone https://github.com/JacobNatural/holiday-manager.git
cd holiday-manager
```

### Running the application:
- To build the application and run it using Docker Compose:
```bash
docker-compose up -d --build
```

### Access to the Swagger UI:
```bash
http://localhost:8080/swagger-ui/index.html
```

### Access to the Frontend Application:
```bash
http://localhost:3000
```
## 🔎 How It Works

The Holiday Manager system is structured around two user roles: **Worker** and **Admin**.

### 👤 Worker Role

Each worker has access to their personal account, where they can:

- View their profile and remaining vacation hours.
- Submit vacation requests by providing the date range and duration.
- Monitor the status of submitted requests (e.g., pending, approved, rejected).

This ensures that employees have full visibility and control over their vacation balance and request history.

### 🛠️ Admin Role

Admins are responsible for overseeing and managing the holiday system. They can:

- View and manage all vacation requests across the system.
- Approve or reject requests submitted by workers.
- Assign or update available vacation hours for users.
- Modify user roles (e.g., promote to admin or revert to worker).

The admin dashboard provides a centralized way to manage the workflow efficiently and transparently.

### 🧠 Smart Features

- **Request Filtering & Sorting**: Easily filter and sort vacation requests by date, user, or status (e.g., ascending/descending).
- **Secure Role-Based Access**: Actions and endpoints are strictly protected via JWT and Spring Security.
- **Validation & Consistency**: All requests go through robust validation layers to maintain data integrity.

## Contributing
We welcome contributions to improve the Shop Statistic Application. Here's how you can contribute:

1. Fork the repository on GitHub.
2. Make enhancements or fix issues in your forked copy.
3. Submit a pull request to merge your changes into the main repository.

Please ensure your code adheres to our coding standards and is thoroughly tested before submitting a pull request.

