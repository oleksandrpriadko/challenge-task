# Technical Task 
[Entain_Technical_Task_Android_2024_.docx](https://github.com/user-attachments/files/17501296/Entain_Technical_Task_Android_2024_.docx)
## General description of the solution
- I replicated behaviour of the webpage you referenced to: https://www.neds.com.au/next-to-go
- UI refreshed every 1 second (customizable within the code)
- Request to backend triggers every 1 minute (customizable within the code)
- MVVM, Api, DataSource, Repository, UseCase

## Libraries and frameworks
- Ktor for requests to the backend
- Kotest + Mockk for unit testing
- Kotling datetime for date/time manipulations
- Kermit for logging
- Jetpack compose for UI
- Material design, no custom components
- Compose previews in WholeScreen.kt

## Structure
- Kotlin DSL
- buildSrc contains common constants for build.gradle.kts files
- version catalog
- Multimodule structure (app, component:shared, component: backend, presentation: shared)



# Unit tests
## Backend component
<img width="653" alt="Screenshot 2024-10-24 at 3 26 19 pm" src="https://github.com/user-attachments/assets/dd9e2a2d-0a38-42f0-9cf6-de2e0c514d59">

## App module
<img width="649" alt="Screenshot 2024-10-24 at 3 27 41 pm" src="https://github.com/user-attachments/assets/0cad67e1-f5c5-474f-989c-1a23660cd00e">

# Documentation
I have added comments around the places that might be difficult to understand at first sight

# Linting
<img width="1893" alt="Screenshot 2024-10-24 at 3 10 35 pm" src="https://github.com/user-attachments/assets/45568866-ef75-414b-8dce-032c9719462b">

# Styling
Standart code style and improt optimisation applied

# Accessibility
- Standart accessiblity applied and tested (I did not modify contentDescription for countdown timer, can do if needed for challenge task)
- Scalable layout






