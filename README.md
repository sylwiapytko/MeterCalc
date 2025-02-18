# MeterCalc - Utility Bill Calculator 
MeterCalc helps users track and calculate monthly utility bills based on manual meter readings.

##  Features (Planned & Implemented)
- [ ] Input meter readings for water, heating, electricity.
- [ ] Input price per unit for each meter
- [ ] Calculate monthly usage.
- [ ] Generate message with calculated data
- [ ] Save & sync data to Google Drive.
- [ ] Generate reports for past months.

##  Tech Stack
- **Language:** Kotlin
- **Frontend:** Jetpack Compose (Modern UI)
- **Backend:** Firebase Firestore or Google Drive API
- **Tools:** Android Studio, Git

##  Data Flow
1. User inputs meter readings and prices  → Stored in Firestore.
2. Monthly usage calculated → Displayed in UI.
3. User exports data → Saved as CSV in Google Drive.

##  Package Structure
- `ui/` → Jetpack Compose views
- `data/` → Database models & Firestore integration
- `logic/` → Business logic & calculations

##  UI Screens
###  Meter Input Screen
- Users enter their monthly readings for different utilities.
- Fields: Water, Heating, Electricity.
- Button: "Calculate Usage"
  
###  Price Management Screen
- Users set and update the price per unit for each utility.
- Ability to adjust prices during different months.

###  Summary Screen
- Displays monthly usage calculations.
- Graph for visualizing past usage trends.

##  Roadmap / Future Plans
- [ ] Support multiple meters per household.
- [ ] Support multiple households in one account.
- [ ] Export reports as PDF/CSV.

