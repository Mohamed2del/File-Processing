### Project Overview

This project is built as a secure document management system using Spring Boot, providing user authentication and authorization with JWT tokens, managing documents with encryption, and offering an efficient API for document-related operations. The application uses H2 in-memory database for fast and simple development setup.

### Key Features and Implementation Details

#### User Authentication and Authorization

**JWT Token-Based Authentication**: The system uses JSON Web Tokens (JWT) to handle user authentication. Users provide their credentials, and a JWT is generated for them upon successful authentication. This token is then used for accessing protected endpoints in subsequent requests.

**Role-Based Access Control (RBAC)**: The application implements role-based authorization by using roles to determine access to specific resources. We used `@PreAuthorize` annotations to restrict access to endpoints based on user roles, e.g., only admins can perform certain actions like editing or deleting documents.

#### Document Encryption

**Document Storage Security**: Documents are encrypted before being stored. A hybrid encryption mechanism is used where each document is encrypted with a randomly generated AES key, and this AES key itself is then encrypted with an RSA public key. This ensures that documents are stored securely in the database.

**AES and RSA Encryption**: AES (Advanced Encryption Standard) is used to encrypt the content of the document for its efficiency with large data. RSA is used to encrypt the AES key for added security. This two-layer approach ensures that even if the document data is compromised, it remains encrypted unless the AES key is decrypted.

#### H2 In-Memory Database

**Quick Development and Testing**: The project leverages the H2 in-memory database for rapid development and testing. Since H2 is lightweight and runs in memory, it allows developers to easily experiment with different features without setting up a persistent database.

### Endpoints Overview

#### Authentication and User Management

- **`/api/auth/login` (POST)**: Accepts user credentials and returns a JWT token if the credentials are valid.
- **`/api/auth/signup` (POST)**: Registers a new user with their details.

#### Document Management

- **`/api/documents/store` (POST)**: Stores a new document. The content is encrypted and saved securely.
- **`/api/documents/{id}/encrypted` (GET)**: Retrieves a specific document along with its encrypted AES key.
- **`/api/documents/{id}/decrypt-key` (GET)**: Retrieves the decrypted AES key for a specific document.
- **`/api/documents/{id}/decrypted` (GET)**: Retrieves the decrypted content of a document.
- **`/api/documents/search` (GET)**: Searches documents based on a provided query.
- **`/api/documents/{id}` (PUT)**: Updates an existing document (Admin only).
- **`/api/documents/{id}` (DELETE)**: Deletes a specific document (Admin only).

#### External API Integration

We have integrated mock APIs to simulate interactions with a fictional external service that provides metadata and classification for documents. These include:

- **Fetch Document Metadata**: Retrieves metadata for a specific document.
- **Extract Document Metadata**: Submits a document for metadata extraction.
- **Document Classification**: Classifies the type of the document.
- **Document Status**: Checks the processing status of a document.

These mock APIs allow us to simulate integration with external services for additional document information.

### Audit Log

The system also includes an audit trail to maintain a log of all user actions, such as document uploads, edits, and deletions. This log is accessible to administrators for review and provides a mechanism for searching and viewing actions performed by users.

### System Monitoring with Actuator

We have implemented monitoring using Spring Boot Actuator, which provides insights into system health, metrics, and user activity. Custom metrics have also been added to track specific actions such as login attempts, document uploads, and edits. These metrics can be integrated with monitoring tools like Grafana in the future.

### Summary

This system ensures secure document management by employing modern security practices, including JWT-based authentication, role-based authorization, and robust document encryption. It provides a straightforward API that allows users to upload, search, and manage documents securely, while administrators can perform higher-level actions such as editing and deleting documents. Additionally, system monitoring, audit logging, and external service integration enhance the overall functionality and security of the application.
Note : ChatGPT was used heavily for this project but wisely ! 