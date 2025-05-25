---

```markdown
# IoTBay Web Application

This Java-based web application simulates an IoT device ordering system. It provides separate functionality for customers (placing orders), staff (managing operations), and system administrators (configuring system settings).
  ```
## Requirements

1. **Java JDK 17**
2. **Apache Tomcat 11**
3. **VS Code** 
4. **Maven** 

## How to Run

1. **Clone the Repository**  
   Open your terminal and run:
   ```bash
   git clone https://github.com/ibrahimraja2324/IoTBay.git

2. **Navigate to the Project Folder**  
   From the root "IoTBay" folder, navigate to the  "assignment1" folder
   ```bash
   cd IoTBay/assignment1
   ```

3. **Build the Project**  
   Clean and package the project using Maven:
   ```bash
   mvn clean package
   ```

4. **Deploy the Application**  
   deploy the contents of the `assignment1` folder:
   - In your IDE (e.g., Vs Code), locate the built `assignment1` folder within the `target` directory.
   - Right-click on the `assignment1` folder and select **"Add to Server"** so that Tomcat can serve it directly.  

5. **Access the Application**  
   Open your web browser and navigate to:
   ```
   http://localhost:8080/assignment1
   ```
   Your application should now be running.
