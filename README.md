# War Machines: Firestorm - A TCP Socket Programming Project

Welcome to War Machines: Firestorm, an online multiplayer game where players control tanks and compete against each other to score points by shooting opponents. This project was undertaken as a part of a group effort to practice socket programming in Java. The game operates on a client-server architecture over TCP.

## Features

- **Tank Control:** Players control individual tanks and aim to shoot opponents to score points.
- **Client-Server Architecture:** The game operates on a client-server model where the server handles all game logic to prevent cheating.
- **Custom TCP Protocol:** We've developed our own TCP packet protocol for communication between clients and the server.
- **Threaded Server:** Each player is assigned a dedicated thread on the server to handle incoming packets, player connection, and disconnection.
- **Movement Rate Limiting:** To prevent server spamming and ensure fair gameplay, movement requests from clients are rate-limited before being processed by the server.
- **Synchronized Movement:** All movement on the server is synchronized to ensure that only one action occurs at a time, avoiding conflicts.
- **Shells Thread:** A separate thread is dedicated to managing the movement of shells (bullets) in the game.
- **Map Updates:** Whenever a change occurs on the server, such as player movement or shooting, the entire layout of the map along with scoreboard information and player names is sent to each client.
- **DataTransferObject:** We utilize a custom DataTransferObject to encapsulate game data and parse it into JSON format for easy transmission.

## Solutions and features we didn't have time to implement
Instead of sending the entire map layout each time a game event occurs, we could have implemented:
1. **Delta Updates**: Calculate the difference (delta) between the current and previous game states and transmit only the changes to reduce network bandwidth usage.
2. **Event-Based Updates**: Send specific events (e.g., player movement, shooting) to clients and let them update their game state accordingly, minimizing data transmission.
3. **Client-Side Prediction**: Implement prediction algorithms on clients to simulate game events locally before receiving updates from the server, reducing perceived latency and network delays.
