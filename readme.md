# 💬 Java Multi-Client Chat Application

A simple real-time chat application built using **Java Socket Programming**.
This project demonstrates core networking concepts such as **TCP communication, multithreading, and message broadcasting**, similar to how basic messaging systems (like Telegram/WhatsApp) work.

---

## 🚀 Features

* 🔗 TCP-based client-server communication
* 👥 Multi-client support using threads
* 💬 Real-time message broadcasting
* 🧑 Usernames for identification
* 🟢 Join/leave notifications
* 🖥️ Console-based interface (UI-ready architecture)

---

## 🧠 Concepts Used

* Java Sockets (`ServerSocket`, `Socket`)
* Multithreading (`Runnable`, `Thread`)
* Input/Output Streams (`BufferedReader`, `PrintWriter`)
* Client-server architecture
* Event-based messaging (join/leave)

---

## 📁 Project Structure

```
chatapp/
 └── src/
     └── main/
         └── java/
             ├── myapp/
             │   ├── ServerMain.java
             │   └── ClientMain.java
             │
             └── serverClient/
                 ├── Server.java
                 ├── Client.java
                 └── ClientHandler.java
```

---

## ⚙️ How It Works

1. **Server starts** and listens on port `5000`
2. Each client connects via socket
3. Server assigns a `ClientHandler` thread for each client
4. Messages are:

    * Received from one client
    * Broadcasted to all other connected clients
5. Server also notifies:

    * When a user joins
    * When a user leaves

---

## 🖥️ How to Run

### 1️⃣ Compile the project

Navigate to:

```
src/main/java
```

Then run:

```
javac myapp/*.java serverClient/*.java
```

---

### 2️⃣ Start the server

```
java myapp.ServerMain
```

---

### 3️⃣ Start clients (multiple terminals)

Open **2–4 terminals** and run:

```
java myapp.ClientMain
```

---

## 💬 Example Output

```
[SERVER]: Samuel joined
[SERVER]: Abel joined

Samuel: Hello everyone
Abel: Hi Samuel

[SERVER]: Abel left
```

---

## 🎯 Future Improvements

* 🎨 JavaFX GUI (chat interface)
* 📩 Private messaging (user-to-user)
* 🧾 Message history
* 🔐 Authentication (login system)
* 🌐 Deployable server

---

## 🎨 UI Upgrade (JavaFX Chat Bubbles)

The application now includes a modern chat interface similar to messaging apps like WhatsApp and Telegram.

### ✨ Features Added

* 💬 Message bubbles (left/right alignment)
* 🎨 Different colors for sender and receiver
* 📜 Scrollable chat window
* ⚡ Real-time UI updates using JavaFX threads

### 🧠 How It Works

* Messages are displayed using `VBox` instead of `TextArea`
* Each message is wrapped inside a styled `Label`
* Messages are aligned:

    * Right → current user
    * Left → other users
* UI updates are handled safely using `Platform.runLater()`

### 🧩 Key Components

* `VBox` → holds chat messages
* `HBox` → controls alignment
* `Label` → message content
* `ScrollPane` → scrolling support

---


## 🧑‍💻 Author

* Samuel Lire

---

## 📌 Notes

This project is built for learning purposes to understand how real-time communication systems work at a low level using Java.

---


## ⭐ If you like this project



Give it a ⭐ on GitHub and feel free to improve it!
