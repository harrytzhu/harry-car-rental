### 1. Normal process use cases
#### Car rental
1. Visit the homepage of car rental platform https://harry-car-rental.azurewebsites.net
2. Click to rent a car
3. Select date
4. Select vehicle type
5. Submit and confirm the order after entering the user information
6. Query the order list through the home page
7. Enter the ID number to query
8. Payment of deposit
9. Offline car pick-up, the administrator clicks the user to pick up or return the car on the home page
10. Enter the ID number to query the order
11. Find the previous order, click the user to pick up the car, and this is the end of the car picking up process
#### Return the car
1. When the user returns the car at the offline store, the administrator accesses the homepage of the car rental platform and clicks the user to pick up or return the car
2. The administrator enters the ID number to query the order
3. The administrator finds the order to return the car and clicks the user to return the car
4. The administrator inputs the vehicle damage fee and ticket fee on the create bill page, and then click next
5. Click OK after the administrator confirms that the bill is correct
6. Users can click on the home page to query the bill list
7. Enter the ID number to query
8. Find the previous bill, click to pay the bill or apply for a refund, and then return the car here successfully
### 2. Error input exception case
#### Choose the wrong date when renting a car
1. The start date is later than the return date, and an error will be prompted after clicking next
2. If the start time or return time is empty, an error will be prompted after clicking next
3. Select the date before the current start time or end time, and click next to prompt an error
4. Select the date after 365 days for the start time or end time. Click Next and you will be prompted with an error
#### Wrong user name, mobile phone number and ID number are entered when creating an order for car rental
1. If one of the user's name, mobile phone number and ID number is empty, an error will be prompted after submitting the order
2. Enter the wrong user name and prompt for error after submitting the order
3. Enter the wrong mobile phone number and prompt for error after submitting the order
4. Enter the wrong ID number, and prompt for error after submitting the order
#### The user entered the wrong ID number when querying the order list
1. If the ID number is empty, you will be prompted to report an error after clicking query
2. Enter the wrong ID number, and click query to prompt an error
#### The user entered the wrong ID number when querying the bill list
1. If the ID number is empty, you will be prompted to report an error after clicking query
2. Enter the wrong ID number, and click query to prompt an error
#### The administrator entered the wrong ID number when querying the order list
1. If the ID number is empty, you will be prompted to report an error after clicking query
2. Enter the wrong ID number, and click query to prompt an error
### 3. Observation result use cases
#### List results of vehicle models available for selection
1. After booking the Toyota Camry containing 2022-07-26 twice, query the list of reservations available on 2022-07-26 again, and there is no Toyota Camry anymore
2. If you select a date range that includes 2022-07-26, you cannot find Toyota Camry
3. If you select a date range that does not include 2022-07-26, you can normally query Toyota Camry
#### User query order list results
1. For orders that have not been paid after creation, there is a link to pay deposit under the operation column of the order list
2. For orders that have already paid the deposit, there is no link to pay the deposit in the order list
#### User query bill list results
1. For bills that have not been supplemented or refunded after creation, there is a link to pay bills or apply for refunds under the operation column of the bill list
2. For bills that have been supplemented or refunded, there is no link to pay bills or apply for refunds in the bill list
#### Administrator queries user order list results
1. For orders for which the user has paid a deposit, there is a link for the user to pick up the car under the operation column in this list
2. For orders that the user has picked up, there is a user return link under the operation column in this list
3. The user has returned the car, and there is no user return link in this list for orders that have been supplemented or refunded in the bill