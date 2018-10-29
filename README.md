# Abstract   
Introduction Dr. D is a system to make connection between diabetes patients and a doctor, diabetes patients provide a specific information which help the doctor provide medical care by reply to the patient with the appropriate recipe and nutrition and health guidance, that will make continuous care for the diabetes patients and give them more healthy live.   

Problem Statement   
● The patient need to rapidly visit the doctor to get the approbet care, which loss of time, effort and money.   
● The amount of effort on the doctor to interview each patient and to do the diagnosis at each visit. 

Procedures   
Providing digital platform to facilitates the communication between doctors and diabetes patients and increase the speed of medical decision-making in order to provide the best conditions and appropriate methods to provide specialized health care for patients through a complete electronic system works on smart device applications connect the Doctor and Patient directly without having to go to the hospital to meet with the doctor. The platform will be on Website and Mobile application, the Doctor and the diabetes patients can use the app to reach there dashboard which providing all the service, the other user (Like the Hospital and the Admin) access to the website to manage the process of the platform and monitoring the care procedures. The platform will be built using SQL for the database, an API for the operation of the database (CRUD operations), PHP for the website back-end, and HTML,CSS and JavaScript for the website front-end. For the Mobile app its built for Android device by the Native android development using Java for the back-end and XML for the front-end and connecting to the same API.    

Results   
● Provide the diabetes care for old people and people with special needs in anywhere and any time.   
● Decrease the number of physical visit to the doctor, replace it with digital report.   
● Give the doctor all the information which needed to provide the appropriate care.   
● Decrease the number of patient for the doctor, and provide simple way to respond.  

# Registration and Login  
Main Page Activity:   
The first Interface provide to the user is the Main Page which contain a Login form and Registration Button, the process of Login is the user will provide the username and password and click the Login Button, the back-end will create a new connection to the API throw the ConnectionToken class to request the security key (Token) with the authentication information to get the access to the API, the API will respond with a Token give the access for a limited time then it will send another request to the User_Inforamtion table to check if the provided username are exist on the database or not, if yes it will get the password and compare it with the provided password if it matched it will redirect the user to next Activity, if it’s not matched a error message will appear. The Registration Button will redirect the user to the Registration Activity.     
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/Main.PNG" alt="Main" width="350" height="700">  
Main Registration Activity:   
After the new patient click on the Registration Button the Registration activity will show a form to provide the Email, username, password and confirm the password and check the Terms & Privacy box and by click on the Register Button it will check if the username already exist on the system or not by pass the username to the User_Information table and check the response, if not it will match the password with confirm password then post the information to the API with the table URL and get the record ID which return by API then redirect to the next Activity and include the ID by putExtera(“UserID”), and if it exist it will show error message.  
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/R.png" alt="R" width="350" height="700">  
Complete Registration Activity:   
Get the UserID from the previous activity then show the complete registration form which include the patient’s information from many tables, after the patient complete the form and click Register Button it will check all the required fields if is any empty it will show error message. The Form contin information from the User_Information, User_Address, Identification and Patient tables, these data will decode into Json format separately and each data set will send request to the API with URL of the tables. After Store all the Patient information it will show a message and redirect to the Patient Dashboard and pass the UserID and the PatientID by the putExtera(“UserID”) and putExtera(“PatientID”).  
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/CR1.jpg" alt="R" width="350" height="700"> 
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/CR2.jpg" alt="CR2" width="350" height="700">  
# Patient Dashboard Activity:  
After providing valued login information the application will redirect the patient to the dashboard throw new Intent and pass the userID from the User_Information table to the dashboard activity throw putExtra. The Activity will show a view contain the operation and service for the patient, Four Button to redirect to the service pages MyProfile, MyDoctor, New Report and Report History. There is a Menu Button to access to the ToolBar which provide Settings, Feedback and Logout Buttons. To make the process we need to get the Id of the Patient to personalize the data, so we create a getPatientID method to handle this, its will send a GET request to the API by pass the UserID to the Patient table and get the PatientID. The Activity contain a RecyclerView which show the daily record for the patient, with the record details and can be Scroll-Down to view as match daily records, the code behind that will provide the data throw AsyncTaskLoader which send a GET request to the API to get the records by pass the PatientID to the Patient_History table and include the Report table which they connected by Foreign-Key that will show only the records for this PatientID and pass the date to filter the records for only today records, then the records will store in arrayList and pass it to a RecyclerViewAdapter which build the RecyclerView and keep it up-to-date. Also the Activity contain a BarChart to show the patient the average of the Blood Glucose for the last month (4 bar represent the weeks of the month) and it will show message if the data are not enough to represented, the code behind that will send request to the API for getting the record from the Report table after filter the records for only this patient and sort the record for only the last month, then the dataset will represented.  
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/PDB.PNG" alt="Main" width="350" height="700">  
MyProfile (Patient) Activity:   
This Activity will show the Patient Information which provided on Registration and give him the ability to update it, when the Activity start it will send a get request to the API and specify the tables which store the Patient Information and pass the UserId and the PatientID.   
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/Main.PNG" alt="Main" width="350" height="700">  
MyDoctor (Patient) Activity:  
If the Patient a new one the Activity will show a search fields which the Patient should provide the code of his Doctor, after click the Button the Activity will send a request to the API with the Code to get the Doctor Information and show it to the Patient and show a select Button to select this doctor and a request will sent to the API to store the information of both the Patient and Doctor to a Specialist table.  
New Report (Patient) Activity:  
On the Start of this Activity it will check if the Patient select the Doctor or not and show a message if not, the Activity will show a Report form to submit to the Doctor the Patient should fill all the required fields and click the submit Button, the Activity will send a post request to the API and specify the URL of the tables and pass the data as Json with the PatientID and his DoctorID, then show message of save the data and redirect the Patient to his Dashboard.  
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/PR.PNG" alt="Main" width="350" height="700">  
History (Patient) Activity:  
This Activity will show a RecyclerView which contain all the report submitted by the Patient with all its information, the code behind that will provide the data throw AsyncTaskLoader which send a GET request to the API to get the records by pass the PatientID to the Patient_History table and include the Report table that will show only the records for this PatientID, so the records of the Patient with his ID will store in arrayList and pass it to a RecyclerViewAdapter which build the RecyclerView and keep it up-to-date.  
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/PH.PNG" alt="Main" width="350" height="700">  

# Doctor Dashboard Activity:  
After providing valued login information the application will redirect the Doctor to the dashboard throw new Intent and pass the userID from the User_Information table to the dashboard activity throw putExtra. The Activity will show a view contain the the operation and service for the Doctor, Two Button to redirect to the service pages MyProfile and MyPatients. There is a Menu Button to access to the ToolBar which provide Settings, Feedback and Logout Buttons. To make the process we need to get the Id of the Doctor to personalize the data, so we create a getDoctorID method to handle this, its will send a GET request to the API by pass the UserID to the Doctor table and get the DoctorID. The Activity contain a RecyclerView which show the new record from the Patients which need a Respond, with the record details and can be Scroll-Down to view as match new records received, the code behind that will provide the data throw AsyncTaskLoader which send a GET request to the API to get the records by pass the DoctorID to the Patient_History table and include the Report table and the Respond table which they connected by Foreign-Key that will show only the records for this DoctorID and check the records on the Respond table if It’s not exist that mean the doctor not respond yet, so the records of the Patient with his ID will store in arrayList and pass it to a RecyclerViewAdapter which build the RecyclerView and keep it up-to-date.  
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/DDB.PNG" alt="Main" width="350" height="700">  
MyProfile (Doctor) Activity:  
This Activity will show the Doctor Information which provided on Registration and give him the ability to update it, when the Activity start it will send a get request to the API and specify the tables which store the Doctor Information and pass the UserId and the DoctorID.  
MyPatients (Doctor) Activity:  
This Activity will show a RecyclerView which contain all the Patient information for this DoctorID, the code behind that will provide the data throw AsyncTaskLoader which send a GET request to the API to get the Patients by pass the DoctorID to the Specialist table and include the Patient table that will show only the Patients for this DoctorID, so the Patients information will store in arrayList and pass it to a RecyclerViewAdapter which build the RecyclerView and keep it up-to-date.
<img src="https://github.com/KAsiri/DrD-App1.0/blob/master/App%20ScreenShot/DR.PNG" alt="Main" width="350" height="700">  
