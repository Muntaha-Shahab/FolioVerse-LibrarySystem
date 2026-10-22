# **FolioVerse - Android Library System App**

**FolioVerse** is a full-featured Android-based Library System developed using **Java** and **XML**, with **Firebase** integration. It supports secure authentication and role-based portals for **admins** and **users**.

Built as a collaborative project by **Muntaha Shahab**, **Tehreem Naveed**, and **Eman Hamid**, the app enables seamless library management with a clean UI and structured navigation.

---

## **Key Features**

### **Authentication (Firebase)**
- **Sign Up using Email & Password**
  - User provides: Username, Gender, Date of Birth
- **Sign In / Login**
  - Via Email & Password  
  - Or via Google Sign-In
- **Forgot Password**
  - Password reset email sent using Firebase
- **Automatic Role Detection**
  - After login, user is redirected based on stored Firebase role:
    - **Admin Portal**
    - **User Portal**
  - Email is auto-fetched when using Google Sign-In
  - Secure Firebase Authentication and role validation

---

### **Admin Portal Features**
- Add user  
- Remove user  
- Add, remove, and update books  
- View all book records  

**Profile:**
- Show Profile  
- Update Profile  
- Forget/Change Password  
- Logout  

---

### **User Portal Features**
- Search books  
- View detailed book information  
- View notifications  
- View trending books *(currently random, locally deployed)*  

**Profile:**
- Show Profile  
- Update Profile  
- Forget/Change Password  
- Logout  

---

### **Profile Panel Details**
- Side navigation drawer includes a profile header displaying:
  - Username  
  - Email  
- Profile view displays:
  - Name, Username, Email, Date of Birth, and Role
- Profile options are consistent across Admin and User portals:
  - Show Profile  
  - Update Profile  
  - Forget/Change Password  
  - Logout  

---

## **Tech Stack**
- **Language:** Java  
- **UI Design:** XML  
- **Platform:** Android Studio  
- **Backend:** Firebase  
  - Firebase Authentication  

---

## **Team Members**
- **Muntaha Shahab**  
- **Tehreem Naveed**  
- **Eman Hamid**
