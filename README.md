# Kaizen 📆🎯

Welcome to **Kaizen (Innerchange)**, a Compose Multiplatform **(CMP/KMP)** application designed to let you create **yearly challenges** with your friends so they can have your back! 🚀

Changes do not happen in one day. Not even in one week or one month.
That's why with Kaizen you commit yourself into something **you love** for **one year**, **each day**, with **no break**.

This project is both a tool to put a deep change in your life and a great way to bound with your friends (you'll be by their sides for one year at least!).

⚠️ Be careful, this challenge is not meant for everyone. If you really want to add something new in your life, something you've missed for a long time and not succeeded to achieve, Kaizen could be the extra push you need. But do not forget, it is for ONE year.

<br>

## ✨ Features

- **Account creation**: Choose a unique avatar and start your journey.
- **Add friends**: Add as much friends as you want to support you in this difficult challenge.
- **Create yearly challenges**: Set up a challenge for 365 days
- **Real-time tracking**: Check if your friends have completed their tasks for the day.
- **Notifications** (TODO): Receive reminders for your ongoing challenges and be notified when your friends are about to fail!.

<br>

## 🎥 Preview

<!-- <div style="text-align: center;">
  <img src="assets/screenshots/app_preview_1.png" alt="Screenshot 1" style="width: 45%; margin: 0 10px;">
  <img src="assets/screenshots/app_preview_2.png" alt="Screenshot 2" style="width: 45%; margin: 0 10px;">
</div> -->
// TODO

<br>

## 🛠️ Technologies and Architecture

Kaizen is built with modern tools and proven concepts to ensure maintainability, performance, and scalability.

### Framework and Language

- **Compose Multiplatform** with Kotlin: A powerful cross-platform framework used here for both Android (stable) and iOS (bêta).

### Libraries and Tools

- **Firebase Firestore + Functions**: Act as a SAAS back-end. Everything is going through functions to ensure a sufficient level of security.
- **Firebase authentication**: For email sign-in and sign-up.
- **Room**: To ensure an offline-first mobile experience.
- **Koin**: For dependency injection.

### Architecture

The project follows **Clean Architecture** principles for clear separation of concerns:

- **Presentation Layer**: Compose UI with ViewModel using MVI pattern.
- **Domain Layer**: Use cases / services for business rules.
- **Data Layer**: Repositories and data sources.

<br>

## 🤝 Contributing

Contributions are welcome! If you have an idea, improvement, or bug fix, feel free to:

1. **Fork the project**.
2. **Create a new branch for your changes**:
   ```
   git checkout -b feature/feature-name
   ```
4. **Submit a Pull Request**.

Thank you for your help! 🙌