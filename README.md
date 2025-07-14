# Inventory Management System

![Inventory Dashboard](https://img.shields.io/badge/Java-Swing-blue?style=flat-square)
![Version](https://img.shields.io/github/v/release/mailktayyabali/Inventory-management-system-?style=flat-square)
![License](https://img.shields.io/github/license/mailktayyabali/Inventory-management-system-?style=flat-square)

## 🚀 Overview

The **Inventory Management System** is a robust and professional desktop application designed to streamline all aspects of inventory, sales, and supplier management for businesses. Built with Java Swing, it offers a modern user experience and modular architecture to ensure scalability, reliability, and security.

---

## 🎯 Main Features

- **Dashboard:** Central hub for navigating all modules with role-based access.
- **User Management:** Add, edit, and manage user accounts with permissions.
- **Product Management:** Manage products, categories, pricing, and stock levels.
- **Category Management:** Organize products into categories for easy access.
- **Supplier Management:** Add and track suppliers, supplier details, and purchase history.
- **Customer Management:** Track customers and their orders.
- **Point of Sale (POS):** Fast and efficient sales processing with invoice generation.
- **Purchase Management:** Record and manage product purchases from suppliers.
- **Sales Management:** View and analyze sales transactions and reports.
- **Invoice System:** Generate, store, and retrieve invoices with full detail.
- **Reporting:** Comprehensive reporting on sales, stock, and suppliers.

---

## 🏗️ Technologies Used

- **Java**: Core language for backend logic and GUI.
- **Java Swing**: For building a responsive user interface.
- **JDBC & SQL**: For database connectivity and operations.
- **MVC Architecture**: Separation of concerns for maintainable code.
- **Custom DAO Classes**: Handling all data access (e.g., `ProductDAO`, `InvoiceDAO`, `CustomerDAO`).

---

## 📦 Project Structure

```
inventoryms/
├── src/
│   ├── Backend/          # Core business logic (Product, Pointofsale, etc.)
│   ├── DB/               # Data Access Objects (InvoiceDAO, ProductDAO, etc.)
│   ├── gui/              # All GUI components (Dashboard, POS, Management GUIs)
│   └── Main.java         # Application entry point
└── README.md             # Project documentation
```

---

## 🖥️ Modules Breakdown

- **Product**: Handles product details, stock levels, and categorization.
- **Point of Sale**: Manages sales transactions, generates invoices, and records customer purchases.
- **InvoiceDAO**: Manages invoice CRUD operations and links invoices to products and customers.
- **SupplierManagementGUI**: Interface for supplier operations.
- **InventoryDashboard**: Main window, links all modules and provides quick navigation.

---

## 🛠️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/mailktayyabali/Inventory-management-system-.git
   ```

2. **Open in your Java IDE**
   - Recommended: IntelliJ IDEA, Eclipse, or NetBeans.

3. **Configure Database**
   - Create a MySQL or PostgreSQL database.
   - Update your database credentials in the DB connection classes (found in `src/DB/DBconnection.java`).

4. **Build & Run**
   - Compile all Java files.
   - Run `Main.java` to start the application.

---

## 📋 Usage

- **Login** with your credentials.
- Navigate using the sidebar to manage users, products, categories, suppliers, customers, purchases, sales, and POS.
- Generate invoices and view reports directly from the dashboard.

---

## 💡 Contribution

We welcome contributions! Please fork the repository, create a pull request, and follow the standard Java coding conventions.

---

## 📝 License

This project is licensed under the MIT License.

---

## 🙏 Credits

Developed by [mailktayyabali](https://github.com/mailktayyabali) 

---

## 📞 Contact

For issues, suggestions, or feature requests, please open an issue or contact via GitHub.

---

> **Professional, scalable, and modern inventory management for your business needs.**
