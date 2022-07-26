## API list
### General
The structure of all API return bodies is like this. The HTTP status code is 200. If the status in the response body is equal to 0, it means success, and if it is not 0, it means failure. Errmsg outputs the failure reason. The data field is the response body data when it succeeds
```
{
    "status":0,
    "errMsg":null,
    "data":{
    }
}
```
### User management
|Field name | description|
|  ----  | ----  |
|ID | user ID|
|Name | name|
|IDnumber | ID number|
|Phonenumber | mobile number|
1. Create user post /users
- Request Body:
```
{
    "Name": "Li Lei", // required
    "IDnumber": "440305202207240001", // required
    "Phonenumber:" 138888888 "// required
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id": 1,
        "name":"Li Lei",
        "idNumber":"440305202207240001",
        "phoneNumber:"13888888888"
    }
}
```
2. Query user get /users/{userid}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id": 1,
        "name":"Li Lei",
        "idNumber":"440305202207240001",
        "phoneNumber:"13888888888"
    }
}
```
3. Modify user put /users/{userid}
- Request Body:
```
{
    "Name": "Li Lei Lei", // not required
    "IDnumber": "440305202207240001", // not required
    "Phonenumber:" 138888888 "// not required
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id": 1,
        "name":"Li Lei Lei",
        "idNumber":"440305202207240001",
        "phoneNumber:"13888888888"
    }
}
```
2. Delete user delete /users/{userid}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":null
}
```
### Order management
|Field name | description|
|  ----  | ----  |
|ID | order ID|
|Userid | user ID|
|Carid | vehicle ID|
|Carmodelid | vehicle model ID|
|StartDate | start date|
|Enddate | end date|
|Deposit | deposit|
|Status | status|
1. Create order post /orders
- Request Body:
```
{
    "Userid": 1, // required
    "Carid": 1, // required
    "Carmodelid:1, // required
    "Startdate:" 2022-07-26 ", // required
    "Enddate:" 2022-07-26 ", // required
    "Deposit:1000 // required
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id":1,
        "userId":1,
        "carId":1,
        "carModelId:1,
        "startDate:"2022-07-26",
        "endDate:"2022-07-26",
        "deposit:1000
    }
}
```
2. Query order get /orders/{orderid}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id":1,
        "userId":1,
        "carId":1,
        "carModelId:1,
        "startDate:"2022-07-26",
        "endDate:"2022-07-26",
        "deposit:1000
    }
}
```
3. Modify order put /orders/{orderid}
- Request Body:
```
{
    "Userid": 1, // not required
    "Carid": 1, // not required
    "Carmodelid:1, // not required
    "Startdate:" 2022-07-26 ", // not required
    "Enddate:" 2022-07-26 ", // not required
    "Deposit:1000 // not required
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id":1,
        "userId":1,
        "carId":1,
        "carModelId:1,
        "startDate:"2022-07-26",
        "endDate:"2022-07-26",
        "deposit:1000
    }
}
```
4. Delete order /orders/{orderid}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":null
}
```
5. Query the order according to the user's ID number get /orders/idnumbers/{idnumber}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":[
        {
            "id":1,
            "userId":1,
            "carId":1,
            "carModelId:1,
            "startDate:"2022-07-26",
            "endDate:"2022-07-26",
            "deposit:1000
        }
    ]
}
```
6. Other post /orders/actions of orders other than CRUD
- Request Body:
```
{
    "ActionID": "create_order_check", // required
    "data":{
        // Optional
    }
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":null
}
```
### Bill management
|Field name | description|
|  ----  | ----  |
|ID | bill ID|
|Userid | user ID|
|OrderID | order ID|
|Carmodelid | vehicle model ID|
|Actualreturndate | actual return date|
|Expiredays | overdue days|
|Rentcost | car rental fee|
|Cardamagecost | vehicle damage cost|
|Fines | ticket amount|
|Totalcost | total cost|
|Ispaid | whether paid true: paid false: not paid|
|Addamount | supplementary payment amount|
|Returnamount | refund amount|
1. Create bill post /bills
- Request Body:
```
{
    "Userid": 1, // required
    "OrderID": 1, // required
    "Carmodelid:1, // required
    "Actualreturndate:" 2022-07-26 ", // required
    "Expiredays:1, // required
    "Rentcast": 300, // required
    "Cardamagecost": 100, // required
    "Fines": 200, // required
    "Totalcost": 1050, // required
    "Addamount": 0, // required
    "Returnamount": 2250, // required
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id":1,
        "userId":1,
        "orderId":1,
        "carModelId:1,
        "actualReturnDate:"2022-07-26",
        "expireDays:1,
        "rentCost":300,
        "carDamageCost":100,
        "fines":200,
        "totalCost":1050,
        "addAmount":0,
        "returnAmount":2250,
    }
}
```
2. Query bill get /bills/{billid}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id":1,
        "userId":1,
        "orderId":1,
        "carModelId:1,
        "actualReturnDate:"2022-07-26",
        "expireDays:1,
        "rentCost":300,
        "carDamageCost":100,
        "fines":200,
        "totalCost":1050,
        "addAmount":0,
        "returnAmount":2250,
    }
}
```
3. Modify bill put /bills/{billid}
- Request Body:
```
{
    "Userid": 1, // not required
    "OrderID": 1, // not required
    "Carmodelid:1, // not required
    "Actualreturndate:" 2022-07-26 ", // not required
    "Expiredays:1, // not required
    "Rentcast": 300, // not required
    "Cardamagecost": 100, // not required
    "Fines": 200, // not required
    "Totalcost": 1050, // not required
    "Addamount": 0, // not required
    "Returnamount": 2250, // not required
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "id":1,
        "userId":1,
        "orderId":1,
        "carModelId:1,
        "actualReturnDate:"2022-07-26",
        "expireDays:1,
        "rentCost":300,
        "carDamageCost":100,
        "fines":200,
        "totalCost":1050,
        "addAmount":0,
        "returnAmount":2250,
    }
}
```
4. Delete bill /bills/{billid}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":null
}
```
5. Query the bill according to the user's ID number get /bills/idnumbers/{idnumber}
```
{
    "status":0,
    "errMsg":null,
    "data":[
        {
            "id":1,
            "userId":1,
            "orderId":1,
            "carModelId:1,
            "actualReturnDate:"2022-07-26",
            "expireDays:1,
            "rentCost":300,
            "carDamageCost":100,
            "fines":200,
            "totalCost":1050,
            "addAmount":0,
            "returnAmount":2250,
        }
    ]
}
```
6. Other post /bills/actions of non crud bills
- Request Body:
```
{
    "ActionID": "pay_bill", // required
    "data":{
        //Optional
    }
}
```
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":null
}
```
7. Query temporary bill get /bills/temporarybill/orders/{orderid}
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":{
        "userId":1,
        "orderId":1,
        "carModelId:1,
        "actualReturnDate:"2022-07-26",
        "expireDays:1,
        "rentCost":300,
        "carDamageCost":0,
        "fines":0,
        "totalCost":0,
        "addAmount":0,
        "returnAmount":0,
    }
}
```
8. Query the official bill get /bills/formalbill/orders/{orderid}? carDamageCost=100&amp;fines=200
- Response Body:
```
{
    "status":0,
    "errMsg":null,
    "data":[
        {
            "id":1,
            "userId":1,
            "orderId":1,
            "carModelId:1,
            "actualReturnDate:"2022-07-26",
            "expireDays:1,
            "rentCost":300,
            "carDamageCost":100,
            "fines":200,
            "totalCost":1050,
            "addAmount":0,
            "returnAmount":2250,
        }
    ]
}
```