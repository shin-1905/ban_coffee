Coffee Shop App

An Android application for managing daily operations in a coffee shop.

The project is developed using Java and provides core management features such as employees, drinks, tables, invoices, and revenue statistics.

Overview

The application is designed to help coffee shop staff and managers manage business operations efficiently through a simple Android interface.

The system includes account authentication and different management modules for handling shop data.

Features
User Authentication
User login
Account management
Basic role management
Employee Management
Add employees
Update employee information
Delete employees
View employee list
Drink Management
Add new drinks
Update drink information
Delete drinks
Search drinks
Manage drink price, category, description, image, and availability
Table Management
Add tables
Edit table information
Delete tables
Manage table status:
Available
In service
Reserved
Invoice Management
View invoices
View invoice details
Store order information
Revenue Statistics
Daily revenue
Monthly revenue
Yearly statistics
Technologies
Java
Android Studio
Android SDK
SQLite
Room Database
RecyclerView
ViewBinding
LiveData
ViewModel
Glide
Material Design
Architecture

The project combines traditional Android components with modern Android architecture components.

For several modules, the application follows an MVVM-style structure:

UI
|
|-- Activity / Fragment
|-- ViewModel
|-- Repository
|-- DAO
|-- Database

This structure helps separate UI logic from data access and improves code maintainability.

Database

The application manages data for:

Accounts
Employees
Drinks
Tables
Shopping Carts
Cart Details
Invoices
Invoice Details

SQLite and Room Database are used for local data storage.

Project Structure
Coffee Shop Management
|
|-- Authentication
|-- Employee Management
|-- Drink Management
|-- Table Management
|-- Invoice Management
|-- Revenue Statistics
|-- Local Database
Getting Started
Requirements
Android Studio
JDK 11 or later
Android SDK
Android device or emulator
Installation
Clone the repository:
git clone <your-repository-url>
Open the project in Android Studio.
Wait for Gradle to finish syncing.
Run the application on an Android emulator or physical device.
Project Purpose

This project was developed as an academic Android application to practice:

Android application development with Java
CRUD operations
SQLite and Room Database
Android UI development
RecyclerView
MVVM architecture concepts
Database design
Business management workflows
Future Improvements
Unified database architecture
Improved role-based authorization
Improved UI/UX
Complete order and payment workflow
Inventory management
Cloud database integration
REST API integration
Advanced revenue charts
Report export
