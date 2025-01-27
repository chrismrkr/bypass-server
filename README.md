# Request Sequential Processor

Http Request Sequential Process using Event Stream & Message Queue

Blog: https://okkkk-aanng.tistory.com/38

![Image](https://github.com/user-attachments/assets/ed77cbfd-2e50-4205-a302-41ffdefad216)



## Demo in Local

### 1. Create Imaage
#### Step 1. clone project
#### Step 2. Create Jar file : ```./gradlew clean build -x test```
#### Step 3. Build image : ```docker build -t queued-event-process-api ./```

### 2. Run in Docker Compose
#### ```docker compose up```

### 3. How to Demo
#### Request Item Create API, ShoppingPoint API

```
# Create Item
POST http://localhost:80/api/item
{
    "itemName": "myItem",
    "price": 1,
    "stock": 100000000
}
```

```
# Create Add Member Shopping Point
POST http://localhost:80/api/shoppingpoint
{
    "memberId": 1,
    "point": 20000000
}
```

#### Request Item Order

```
PATCH http://localhost:80/api/item
{
    "memberId": 1,
    "itemId": 1,
    "amount": 1000
}
```

```
# Sequential Process API
PATCH http://localhost:80/api/event/item
{
    "memberId": 1,
    "itemId": 1,
    "amount": 1000
}
```
