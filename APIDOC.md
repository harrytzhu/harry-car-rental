## API列表
### 通用
所有API返回体结构都是这样，Http Status Code都是200，响应体中的status等于0表示成功，非0表示失败，errMsg输出失败原因，成功时data字段为响应体数据
 ```
 {
    "status":0,
    "errMsg":null,
    "data":{
        
    }
 }
 ```
### 用户管理

|  字段名   | 描述  |
|  ----  | ----  |
| id  | 用户ID |
| name  | 姓名 |
| idNumber | 身份证号 |
| phoneNumber | 手机号 |

1. 创建用户 POST /users
- Request Body:
 ```
 {
    "name":"Li Lei", //必选
    "idNumber":"440305202207240001", //必选
    "phoneNumber:"13888888888" //必选
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
2. 查询用户 GET /users/{userId}
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
3. 修改用户 PUT /users/{userId}
- Request Body:
 ```
 {
    "name":"Li Lei Lei", //非必选
    "idNumber":"440305202207240001", //非必选
    "phoneNumber:"13888888888" //非必选
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
2. 删除用户 DELETE /users/{userId}
- Response Body:
 ```
 {
    "status":0,
    "errMsg":null,
    "data":null
 }
 ```

### 订单管理

|  字段名   | 描述  |
|  ----  | ----  |
| id  | 订单ID |
| userId  | 用户ID |
| carId | 车辆ID |
| carModelId | 车辆型号ID |
| startDate | 开始日期 |
| endDate | 结束日期 |
| deposit | 押金 |
| status | 状态 |

1. 创建订单 POST /orders
- Request Body:
 ```
 {
    "userId":1, //必选
    "carId":1, //必选
    "carModelId:1, //必选
    "startDate:"2022-07-26", //必选
    "endDate:"2022-07-26", //必选
    "deposit:1000 //必选
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
2. 查询订单 GET /orders/{orderId}
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
3. 修改订单 PUT /orders/{orderId}
- Request Body:
 ```
 {
    "userId":1, //非必选
    "carId":1, //非必选
    "carModelId:1, //非必选
    "startDate:"2022-07-26", //非必选
    "endDate:"2022-07-26", //非必选
    "deposit:1000 //非必选
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
4. 删除订单 DELETE /orders/{orderId}
- Response Body:
 ```
 {
    "status":0,
    "errMsg":null,
    "data":null
 }
 ```
5. 根据用户身份证号查询订单 GET /orders/idNumbers/{idNumber}
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
6. 订单非CRUD的其他 POST /orders/actions
- Request Body:
 ```
 {
    "actionId":"CREATE_ORDER_CHECK", //必选
    "data":{
        //非必选
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

### 账单管理

|  字段名   | 描述  |
|  ----  | ----  |
| id  | 账单ID |
| userId  | 用户ID |
| orderId | 订单ID |
| carModelId | 车辆型号ID |
| actualReturnDate | 实际还车日期 |
| expireDays | 超期天数 |
| rentCost | 租车费用 |
| carDamageCost | 车损费用 |
| fines | 罚单金额 |
| totalCost | 总费用 |
| isPaid | 是否已支付 true:已支付  false:未支付 |
| addAmount | 补交金额 |
| returnAmount | 退款金额 |

1. 创建账单 POST /bills
- Request Body:
 ```
 {
    "userId":1, //必选
    "orderId":1, //必选
    "carModelId:1, //必选
    "actualReturnDate:"2022-07-26", //必选
    "expireDays:1, //必选
    "rentCost":300, //必选
    "carDamageCost":100, //必选
    "fines":200, //必选
    "totalCost":1050, //必选
    "addAmount":0, //必选
    "returnAmount":2250, //必选
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
2. 查询账单 GET /bills/{billId}
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
3. 修改账单 PUT /bills/{billId}
- Request Body:
 ```
 {
    "userId":1, //非必选
    "orderId":1, //非必选
    "carModelId:1, //非必选
    "actualReturnDate:"2022-07-26", //非必选
    "expireDays:1, //非必选
    "rentCost":300, //非必选
    "carDamageCost":100, //非必选
    "fines":200, //非必选
    "totalCost":1050, //非必选
    "addAmount":0, //非必选
    "returnAmount":2250, //非必选
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
4. 删除账单 DELETE /bills/{billId}
- Response Body:
 ```
 {
    "status":0,
    "errMsg":null,
    "data":null
 }
 ```
5. 根据用户身份证号查询账单 GET /bills/idNumbers/{idNumber}
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
6. 账单非CRUD的其他 POST /bills/actions
- Request Body:
 ```
 {
    "actionId":"PAY_BILL", //必选
    "data":{
        //非必选
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
7. 查询临时账单 GET /bills/temporaryBill/orders/{orderId}
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
8. 查询正式账单 GET /bills/formalBill/orders/{orderId}?carDamageCost=100&fines=200
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
