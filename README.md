# Secure Notes Service

A RESTful backend service built with Spring Boot that demonstrates a secure, end-to-end encrypted note-sharing system.

## Core Concepts

-   **End-to-End Encryption (E2EE):** Architectural design that ensures data is encrypted on the client and can only be decrypted by the intended recipient.
-   **Asymmetric & Symmetric Cryptography:**
    -   **Diffie-Hellman Key Exchange** for establishing a shared secret between users.
    -   Client-side **AES** for content encryption (simulated).
-   **Stateless Authentication:** Secure API protection using **JSON Web Tokens (JWT)** managed by **Spring Security**.
-   **Secure Password Storage:** Password hashing using **BCrypt**.
-   **Clean REST API Design:** Well-defined, resource-oriented endpoints.
-   **Data Persistence:** Use of **Spring Data JPA/Hibernate** with a PostgreSQL database.
-   **Modern Java:** Built with Java 21.

## Tech Stack

-   **Java 21**
-   **Spring Boot 3**
-   **Spring Security 6**
-   **Spring Data JPA**
-   **PostgreSQL**
-   **Maven**

## API Showcase

The following examples demonstrate the core user flow of the application.

---

### 1. Register Users

First, two users, `alice` and `marco`, are registered. The server generates and stores a long-term public key for each.

#### `POST /api/users/register`
**Request:**
```
{
    "username": "alice",
    "password": "password123"
}
```
**Response:**
```
{
    "publicId": "91b19bd2-862a-4a8a-8401-1e0710556a05",
    "username": "alice"
}
```
# 
### 2. User Login

A user logs in to receive a JWT, which is required for accessing protected endpoints.
#### `POST /api/auth/login`

**Request:**
```
{
    "username": "alice",
    "password": "password123"
}
```

**Response:**
```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZSIsImlhdCI6MTY3OT..."
}
```
#
_**All subsequent requests must include this token in the Authorization: Bearer <token> header.**_
#

### 3. Securely Share a Note

Alice (authenticated) sends an encrypted note to Marco. The request includes the encrypted content, Bob's public ID, and Alice's temporary (ephemeral) public key for this specific exchange.
#### `POST /api/notes`
**Request (from Alice, for Marco):**
```
{
    "encryptedContent": "YXNkZmFzZGxvaGFzaGRmbGphaHNkZmxqYWhzZGxmamxhc2tkZmpzYWxka2Zq",
    "recipientPublicId": "UUID-of-marco-here",
    "senderEphemeralPublicKey": "Base64-encoded-ephemeral-public-key-from-alice"
}
```

**Response:**
```
{
    "publicId": "b82f1d9a-f0eb-4889-8732-51e8cf568707",
    "ownerUsername": "alice",
    "recipientUsername": "marco"
}
```

### 4. Fetch Received Notes

Marco (now authenticated with his own JWT) fetches his notes. He sees the note from Alice in his receivedNotes list, which contains all the information his client needs to derive the shared secret and decrypt the content.

#### `GET /api/notes`

Request (from Marco):
```
(No request body)
```

**Response:**
```
{
    "ownedNotes": [],
    "receivedNotes": [
        {
            "publicId": "b82f1d9a-f0eb-4889-8732-51e8cf568707",
            "encryptedContent": "YXNkZmFzZGxvaGFzaGRmbGphaHNkZmxqYWhzZGxmamxhc2tkZmpzYWxka2Zq",
            "ownerUsername": "alice",
            "recipientUsername": "marco",
            "senderEphemeralPublicKey": "Base64-encoded-ephemeral-public-key-from-alice"
        }
    ]
}
```
#

--- 
### Running Locally

- **Prerequisites:** JDK 21, Maven, and PostgreSQL.
- Clone the repository.
- Create a PostgreSQL database and update the connection details in src/main/resources/application.yml.
- Run the application using mvn spring-boot:run.