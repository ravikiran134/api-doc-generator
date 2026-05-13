# API Documentation: UserController

```markdown
# API Documentation

## POST /register
**Description:** Registers a new user based on the provided `RegistrationRequestDTO`.
**Authentication:** None
**Path Variables:** None
**Query Parameters:** None
**Request Body:** 
  - [RegistrationRequestDTO](_See DTO class for exact fields_)
**Response:** 
  - Status: `201 Created`
  - Body: `"User registered successfully."`
**Status Codes:** 
  - `201`: User registration successful.
  - `400`: Invalid input.
Example:
```bash
curl -X POST "http://localhost:8080/register" \
-H "Content-Type: application/json" \
-d '{
  "username": "newuser",
  "password": "securepassword"
}'
```

## GET /getTokens
**Description:** Retrieves all invite tokens.
**Authentication:** None
**Path Variables:** None
**Query Parameters:** None
**Request Body:** 
  - None
**Response:** 
  - Status: `200 OK`
  - Body: List of `[InviteToken]` (See DTO class for exact fields)
**Status Codes:** 
  - `200`: Invite tokens retrieved successfully.
Example:
```bash
curl "http://localhost:8080/getTokens"
```

## POST /login
**Description:** Authenticates a user and returns an access token if the credentials are valid.
**Authentication:** None
**Path Variables:** None
**Query Parameters:** None
**Request Body:** 
  - [LoginRequestDTO](_See DTO class for exact fields_)
**Response:** 
  - Status: `200 OK`
  - Body: `{ "token": "your_access_token" }`
  - Status: `401 Unauthorized`
  - Body: `"Invalid credentials"`
**Status Codes:** 
  - `200`: Authentication successful, token returned.
  - `401`: Invalid username or password.
Example:
```bash
curl -X POST "http://localhost:8080/login" \
-H "Content-Type: application/json" \
-d '{
  "username": "existinguser",
  "password": "securepassword"
}'
```

## POST /addStock
**Description:** (inferred) This endpoint seems to be intended for adding a stock, but it returns `null`.
**Authentication:** `hasAnyRole('USER', 'ADMIN')`
**Path Variables:** None
**Query Parameters:** None
**Request Body:** 
  - None
**Response:** 
  - Status: `200 OK`
  - Body: Null or inferred response body based on method implementation
**Status Codes:** 
  - `200`: Operation successful, but return value is null.
Example:
```bash
curl -X POST "http://localhost:8080/addStock"
```

## GET /users/{userId}/projects
**Description:** Retrieves a list of projects for the user with the specified ID.
**Authentication:** None
**Path Variables:** 
  - `userId`: UUID (User ID)
**Query Parameters:** None
**Request Body:** 
  - None
**Response:** 
  - Status: `200 OK`
  - Body: List of `[ProjectSummaryDTO]` (See DTO class for exact fields)
**Status Codes:** 
  - `200`: Projects retrieved successfully.
Example:
```bash
curl "http://localhost:8080/users/12345678-1234-1234-1234-1234567890ab/projects"
```

---

_See DTO class for exact fields_
```