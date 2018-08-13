# Plant-Shop-App
  This application could be used by customers of a plant shop to find and search for a specific type of plant.  The plants are stored in a mysql database and contain unique row and column attributes that would align to row and column numbers/letters in the actual plant shop.  This concept and application are not just constrained to a plant shop, they could be implemented into any shop wanted.
  The databaseTable file is used to connect to the mysql database to search for and return plants.  This file also creates a GUI for the owners of the store to add, delete, and edit plants.  This GUI is retrieved by typing in a password, plantShop1234, into the search bar of the customor GUI.  This password prevents customers from accesing the database GUI, and connects both of the GUIs into 1 application.  It also allows the shop owner to exit out of the application because the customer GUI prevents the customer from exiting out of the application.  The paintInfo file is used as a means of storing each individual plant.  The plantSearch file sets up the custormer GUI that allows them to search for the plants in the database.  Each file is provided in a JAVA and txt file format.