Application Title : Eco Food

1.	Overview of Eco Food Application
The EcoFood application is a mobile platform designed to reduce food waste through two primary features. Firstly, it enables users to purchase surplus food from restaurants at lower prices, thereby contributing to waste reduction in the culinary industry. Secondly, users can input and monitor food items with expiry dates, allowing them to manage consumption before the food becomes unusable. However, the application currently operates with a single restaurant as a model for food transactions. Consequently, EcoFood application consists of two user perspectives, including the user interface for potential app users and the admin interface for restaurant owners. By integrating economic, social, and environmental aspects, EcoFood supports the achievement of Sustainable Development Goal (SDG) 2 (Zero Hunger) by promoting more equitable food distribution and addressing hunger caused by inefficiencies in the food supply chain.

The benefits of EcoFood are diverse, impacting both individuals and the broader community. For users, the app provides the convenience of accessing quality food at more affordable prices, helping them save on daily expenses. Additionally, the food monitoring feature based on expiry dates encourages users to manage ingredients more wisely, reduce waste, and ensure more efficient consumption. On the other hand, EcoFood aids restaurants in managing surplus food, significantly reducing potential food waste that could harm the environment.

Most of the similar applications provide the ordering food progress, such as Gojek, Grab, and so on. However, the key difference on this application compared with those apps is EcoFood provides a management system of inputing and monitorng food items with expiry dates, allowing the user to manage consumption before the food becomes unconsumable. Moreover, this application implements several Native Android features, such as biometric authentication using fingerprint and mobile PIN, as well as accessing phone camera and storage, specifically in image folder.

3.	Group Members
The members of the group called Empat Cina are as follows.
1)	Jackson Lawrence (00000070612)
Email student : jackson.lawrence@student.umn.ac.id
2)	Jonathan Susanto (00000071412)
Email student : jonathan.susanto@student.umn.ac.id
3)	Louis Gabriel Hernandes (00000070250)
Email student : louis.gabriel@student.umn.ac.id
4)	Glen Owen (00000070244)
Email student : glen.owen@student.umn.ac.id

3.	Application Features
In this application, the progress that has been made has reached final finalization, making this application ready to be used and experienced by potential users. Indeed, the features in this application support potential users to experience an extraordinary experience. The features contained in the EcoFood application from both the user and admin side are as follows.

3.1.	 User Features
1)	Splash Screen
The Eco Food application offers a splash screen as its loading screen. This animation will utterly make the users feel more attractive to this application as it’s the first features that user will see after opening the application. 

2)	Sign In and Sign Up
Sign in and sign up features are very common in application. Both features allow users to manage access to applications. Sign Up is used to register a new account by filling in data such as name, email, phone number, and password, often accompanied by verification for security, and the data saved in the Firebase. Meanwhile, Sign In allows registered users to log in with their credentials that searched from the Firebase. These two features ensure data security and provide access to application features in an organized manner.

3)	Biometric Authentication using Fingerprint or Mobile PIN
After user has logged in, it will redirect to biometric authentication by fingerprint for those who have the fingerprint features in their phone. If not, users will enter by using their mobile PIN. This feature aims to ensure secure access to the application by verifying the user's identity. This process involves methods such as entering an email and password, using two-factor authentication (2FA), or biometrics such as fingerprints and facial scans (Face ID) as it is one of the Native Android features. With this feature, only registered and verified users can access accounts, keeping data and transactions safe. Authentication can also be used to protect access to sensitive features within an application, ensuring maximum privacy and security.

4)	Order Foods
Users are able to order the food and allows them to easily order services or products through the application. In addition, user can choose their food based on categories, like all, main course, desserts, and so on. With this feature, users can select the desired product or service, add to cart, and proceed to the payment process. This feature is equipped with order details such as quantity, food description, food location, and price that has been inputed by admin and saved to Firebase. The order feature is designed to provide a fast, organized and convenient ordering experience for users.

5)	Search Foods
When users want to order their desire foods, they can directly search the food name into the search bar. Users can also search the food name on each category. Each category and food descriptions have structured well inside the Firebase.

6)	My Carts
After users has ordered foods, they can see all of their ordered items in the cart along with the total quantity, quantity in each food, total prices, and price in each item. Users can also add the quantity directly or remove the ordered foods. The carts cannot be removed by itself when users want to visit another page before doing the payment, allowing users can revisit the cart with the items or foods they ordered.

7)	Payments and Add Payment Method
After all the foods have been ordered by the user and saved in the cart, users are then directed to payment. In this part, users can finish the order by their own payment method. To add the payment method, they can add it in profile page and choose what methods to pay. The payment feature provides flexibility for users to choose the appropriate payment method, such as MasterCard or Paypal and saved to Firebase after creation. This feature also displays the total fees to be paid, ensuring transparency before users press the Pay & Confirm button to complete the transaction. After the user confirms the payment and checks the total price, then the payment will be redirect to a page where it has already successful. With an intuitive design, this feature makes it easy for users to manage payments quickly and safely.

8)	Add Foods Item with Expired Date
The Add Item feature allows users to add new items or data to the application system. In this view, users can fill in various important information such as item name, image, location, expiration date, stock quantity, and related descriptions. This process comes with interactive options such as increasing or decreasing stock quantity and entering details manually. Moreover, admin can add item pictures by accessing the phone storage as it is a Native Android feature. After all the information has been filled in, the user can save the data by pressing the Save Changes button. This feature is designed to make it easier to manage data in a structured and efficient manner. The informations where the user has submitted the item will show in the home page where it says which item is nearly expired along with the name and image of the item / food in which it is also saved to the Firebase.

9)	History
The history feature allows users to view the history of transactions or activities that have been carried out in the application. In this feature, users can access details such as transaction date, items purchased, payment amount, payment method, and transaction status (accepted or on progress) that is detected and showed from the Firebase. This feature is designed to provide transparency and ease for users in tracking their activities, both for financial management, documentation and future reference purposes. With a structured view, users can quickly find the information they need from their history.

10)	Change Password
In the profile page of the user, specifically in settings part, it allows user to change password, username, phone number, or username, along with the changes is done in the Firebase. When user wants to log in with the same email and changed password, it can also be entered with the newest password.

11)	 Log Out
In the profile page of the user, users are allowed to log out in profile page that its button locate in the right corner and inside the settings button.

3.2.	 Admin Features
1)	Splash Screen
The Eco Food application offers a splash screen as its loading screen. This animation will utterly make the admin feel more attractive to this application as it’s the first features that user will see after opening the application. 

2)	Sign In 
Sign in is very common in application. The difference with the users is the admin has its own credential to enter the application and manage the stocks. This feature ensures data security and provide access to application features in an organized manner without any two factor authentication since the admin cannot change its own email and password. This mean the admin account is a unique rule in the Firebase to manage all of the foods or items.

3)	Pie Chart
In the homepage of the admin, there is a pie chart that displays income distribution that taken by the calculation inside the Firebase based on several categories of the foods, including all food, desserts, main course, and so on. Each segment on the pie chart represents the proportion of each category in percentage form. This feature makes it easier for admins to analyze the income allocation or contribution of each division visually, enabling the identification of areas that are dominant or that require more attention. With a clear and intuitive display, admins can make strategic decisions based on this data.

4)	Income
In the second page of the admin, it is utilized as a center for data management and decisions regarding orders accepted or rejected by the admin. On this page, admins can see a summary of order statistics, such as the total income of all orders by all users, orders that have been accepted, and orders that have been rejected. The total income is calculated based on the item that is purchased and accepted from the Firebase, allowing admin can see the current income and able to show or hide of the value in the homepage. In addition, admins can directly take action on new orders with the Accept option to continue the process, or reject if the order cannot be processed. This page is designed to help admins manage orders efficiently and provide full control over the order acceptance or rejection process, along with the total prices of the ordered item by the users and it is also displayed in the homepage of the admin.

5)	Add Item / Food
The Add Item page in the admin allows the admin to add new data to the application system, such as products, services, or other items. On this page, admins can fill in various important information such as item name, image, location, stock quantity, and detailed description. There are also interactive features such as buttons to increase or decrease stock amounts. Moreover, admin can add item pictures by accessing the phone storage or using phone camera as both are Native Android features. After all data has been filled in, the admin can save the changes by pressing the Save Changes button and it will save the new item in the Firebase and directly show in the user part, specifically in the order page. This feature is designed to make it easier for admins to manage data in a structured and efficient way, ensuring new information can be added to the system quickly and accurately. 

6)	Change Stocks
In this page, admin is allowed to change directly the stock of the item and remove it if the item is not worth to sell. Moreover, admin can search the item or food that needed to be altered on the number of stocks in the search bar. This mean the item informations will also change inside the Firebase.

7)	 Log Out
In the profile page of the admin, admin is allowed to log out in profile page that its button locate in the right corner and inside the settings button.

4.	Credits
The development of the EcoFood application is supported by various resources and tools that play a pivotal role in its creation. The application is built using Android Studio, an integrated development environment (IDE) provided by Google, ensuring robust performance and seamless user experience. The programming language utilized is Kotlin, chosen for its modern syntax and compatibility with Android platforms, combined with XML as its front-end development. Moreover, there are several links regard on the files of this application along with the video, presentation slides, and so on as for the submission in this final exam. Those links are as follows.

1)	GitHub
https://github.com/Jacksonciek/UAS-Mobile-Application-Programming-Empat-Cina

2)	Google Drive
https://drive.google.com/drive/folders/1ljwgBSQiPtyrFBi6kvVDMvi_5Jf867Gr?usp=sharing

3)	Canva
https://www.canva.com/design/DAGYC89gMyU/ii2Tosq1dC5J8EgJAtKItQ/view?utm_content=DAGYC89gMyU&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h71074277d3

4)	Video (Explanation of Application Overall)
https://drive.google.com/file/d/1WjOVSxChvF-5PVodVIyaYXf3hEaQjo8j/view?usp=sharing

5)	Video (Explanation of Application Flow)
https://drive.google.com/file/d/14Knbh1n8sh62VcFrxGbyBNv660Kzt64T/view?usp=sharing

6)	README
https://docs.google.com/document/d/1giwb-cy9JcVajoOiTe4FfvCRq_-WnHIVACSHTaOTRtg/edit?usp=sharing
