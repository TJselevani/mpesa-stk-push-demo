
# 💳 M-Pesa STK Push Demo (Daraja API + Spring Boot)

This project demonstrates how to integrate **Safaricom’s Daraja API** for **M-Pesa STK Push (Lipa Na M-Pesa Online)** using **Spring Boot**.  
It’s designed to be **clean, modular, and beginner-friendly**, making it a great learning resource or boilerplate for production projects.

---

## ✅ Features

- **A clean modular structure** (Controller → Service → DTO → Config)
- **STK Push initiation + Callback handling**
- **Environment variable configuration** (no hardcoded credentials)
- **Logging + sample JSON responses**
- **Optional database setup** (store and track transactions)
- **Unit test templates** for expandability and maintainability

---

## 🚀 Project Overview

| Property | Details                                           |
|-----------|---------------------------------------------------|
| **Project Name** | `mpesa-stk-push-demo`                             |
| **Tech Stack** | Spring Boot 3.3+, Java 21, Maven                  |
| **Libraries** | Lombok, Validation, Json, Web                      |
| **Optional Add-ons** | MongoDB or PostgreSQL for transaction persistence |

---

## 📁 Project Structure

```

mpesa-stk-push-demo/
├── src/main/java/com/example/mpesa/
│   ├── config/
│   │   └── MpesaConfig.java
│   ├── controller/
│   │   └── MpesaController.java
│   ├── dto/
│   │   ├── StkPushRequest.java
│   │   ├── StkPushResponse.java
│   │   ├── StkCallback.java
│   │   └── CallbackMetadata.java
│   ├── service/
│   │   ├── MpesaService.java
│   │   └── TokenService.java
│   ├── util/
│   │   └── HttpUtil.java
│   └── MpesaApplication.java
├── src/test/java/com/example/mpesa/
│   └── MpesaApplicationTests.java
└── pom.xml

````

---

## 🧠 What is M-Pesa STK Push?

STK Push (Sim Tool Kit Push) allows your app or system to **initiate a payment request** directly to a customer’s phone via M-Pesa.  
The customer then confirms the payment using their M-Pesa PIN.

In this demo:
1. The user enters a **phone number**, **amount**, and **description**.
2. Your app sends the STK Push request to **Safaricom Daraja API**.
3. The user receives an **M-Pesa prompt** on their phone (The phone number they entered).
4. Once confirmed, Safaricom sends a **callback** to your app’s `/callback` endpoint with the transaction details.

---

## 🔑 Getting Started — Setting Up Your Daraja Account

To run this project, you need credentials from Safaricom’s **Daraja Developer Portal**.

---

### 🧭 Step 1: Create a Daraja Developer Account

1. Go to [https://developer.safaricom.co.ke](https://developer.safaricom.co.ke)
2. Click **Sign Up** (or **Login** if you already have an account).
3. Fill in your details (email, password, organization, etc.).
4. Verify your email to activate your account.

---

### ⚙️ Step 2: Create a New App

1. Once logged in, go to **My Apps → Add a New App**.
2. Enter a name, e.g., `Mpesa STK Demo`.
3. Select **MPESA EXPRESS (Lipa Na M-Pesa Online API)**.
4. Choose the **Sandbox** environment (for testing).
5. Save the app.

You’ll get:
- **Consumer Key**
- **Consumer Secret**

Keep these safe — you’ll use them in your `.env` or environment variables.

---

### 🔐 Step 3: Get Your Passkey

1. Go to your app’s **API tab**.
2. Click **M-Pesa Express (Lipa Na M-Pesa Online)**.
3. You’ll find your **Passkey** under that section.
   (Select your application then select test credentials)
4. Copy and store it securely — it’s required to generate the STK password.

---

### 🌍 Step 4: Expose Your Callback URL (Optional for Testing)

During testing, you can use **Ngrok** to make your local callback URL publicly accessible.

```bash
ngrok http 8080
````

Then, set your callback URL to something like:

```
https://your-ngrok-subdomain.ngrok.io/api/mpesa/callback
```

---

## ⚙️ Environment Setup

Create a file named `.env` (or use environment variables directly):

```bash
MPESA_CONSUMER_KEY=your_consumer_key_here
MPESA_CONSUMER_SECRET=your_consumer_secret_here
MPESA_PASSKEY=your_passkey_here
MPESA_SHORTCODE=174379
MPESA_CALLBACK_URL=https://your-ngrok-url/api/mpesa/callback
MPESA_ENVIRONMENT=sandbox
```

Or if you prefer using Spring’s built-in config, edit `application.yml`:

```yaml
server:
  port: 8080

mpesa:
  consumerKey: ${MPESA_CONSUMER_KEY}
  consumerSecret: ${MPESA_CONSUMER_SECRET}
  shortCode: ${MPESA_SHORTCODE:174379}
  passKey: ${MPESA_PASSKEY}
  callbackUrl: ${MPESA_CALLBACK_URL}
  environment: ${MPESA_ENVIRONMENT:sandbox}
```

I'd recommend using Spring’s built-in config for simplicity, edit `application.yml` and replace the variables with the actual values:

---

## ▶️ Running the Project

### 1️⃣ Clone the repository

```bash
git clone https://github.com/your-username/mpesa-stk-push-demo.git
cd mpesa-stk-push-demo
```

### 2️⃣ Build the project

```bash
mvn clean install
```

### 3️⃣ Run the application

```bash
mvn spring-boot:run
```

---

## 💳 Testing the STK Push

Send a POST request to:

```
POST http://localhost:8080/api/mpesa/stkpush
```

With JSON body:

```json
{
  "phoneNumber": "254712345678",
  "accountReference": "TestAccount",
  "description": "Payment for order #1234",
  "amount": 1
}
```

You should receive a response like:

```json
{
  "merchantRequestId": "29115-34620561-1",
  "checkoutRequestId": "ws_CO_DMZ_123456789_0102030405",
  "responseDescription": "Success. Request accepted for processing",
  "responseCode": "0"
}
```

---

## 🔁 Callback Response Example

After the user completes payment, Safaricom will send a callback to your `/callback` endpoint:

```json
{
  "Body": {
    "stkCallback": {
      "MerchantRequestID": "29115-34620561-1",
      "CheckoutRequestID": "ws_CO_DMZ_123456789_0102030405",
      "ResultCode": 0,
      "ResultDesc": "The service request is processed successfully.",
      "CallbackMetadata": {
        "Item": [
          { "Name": "Amount", "Value": 1.00 },
          { "Name": "MpesaReceiptNumber", "Value": "NLJ7RT61SV" },
          { "Name": "TransactionDate", "Value": 20250131120321 },
          { "Name": "PhoneNumber", "Value": 254712345678 }
        ]
      }
    }
  }
}
```

You can view this in the application logs or store it in a database.

---

## 🧩 Optional: Database Integration

You can easily integrate MongoDB or PostgreSQL to store transaction data.

### Example Table (PostgreSQL)

| Field            | Type   | Description             |
| ---------------- | ------ | ----------------------- |
| id               | UUID   | Primary Key             |
| phone_number     | String | Customer’s phone number |
| amount           | Double | Transaction amount      |
| receipt_number   | String | M-Pesa receipt code     |
| transaction_date | Long   | Timestamp from callback |
| result_code      | String | Result from M-Pesa      |
| status           | String | SUCCESS / FAILED        |

---

## 🧪 Testing

Run tests:

```bash
mvn test
```

You can extend `MpesaApplicationTests.java` to test:

* Token retrieval
* STK Push initiation
* Callback deserialization

---

## 🪵 Logging

Logs are printed to the console and include:

* Request & response data for STK push
* Callback payloads
* Token acquisition status

You can later extend this using **SLF4J** or **Spring Boot Actuator** for more advanced observability.

---

## 🛡️ Notes

* Use the **sandbox** environment for testing.
* M-Pesa STK only works on **Safaricom numbers starting with 2547 or 2541**.
* Amounts in sandbox mode are **virtual** — no real money moves.
* In production, update your environment to `production` and configure HTTPS callbacks.

---

## 🤝 Contributing

Pull requests and contributions are welcome!
If you’re using this project for learning, please ⭐ it on GitHub and share improvements.

---

## 📜 License

This project is licensed under the **MIT License** — you are free to use, modify, and distribute it.

---

### 💬 Questions or Feedback?

Feel free to open an **Issue** or reach out with suggestions for improvements or new features.

