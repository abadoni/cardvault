# Card Vault

A simple full-stack application to securely store and search card numbers (PAN) with encryption.  

**Backend:** Java Spring Boot  
**Frontend:** React  

---

## Features

- Add a card with **Cardholder Name** and **PAN**  
- PAN is **encrypted at rest** and **never stored or logged in plaintext**  
- Inline validation for card number (12–19 digits, digits only)  
- Search cards by **full PAN** or **last 4 digits**  
- Masked PAN is displayed in results (e.g., `438512**4342`)  
- Displays success and error messages clearly  

---

## Prerequisites

- Java 17+  
- Maven 3.8+  
- Node.js 18+  
- Git  

---

## Project Structure
cardvault/

├─ src/main/java/... # Spring Boot backend

├─ src/main/resources/

│ ├─ application.properties

├─ ui/ # React frontend

│ ├─ src/

│ │ └─ App.js

│ ├─ public/

│ ├─ package.json

│ └─ package-lock.json

├─ .gitignore

├─ .gitattributes

└─ pom.xml


---

## How to Run Locally

### 1. Backend (Spring Boot)

1. Open Terminal and navigate to project root:
```bash
cd /path/to/cardvault

