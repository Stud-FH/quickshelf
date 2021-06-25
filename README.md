# pizzaServer
spring boot exercise / pizza delivery service api

## how to use this api
### setup
deploy the following file to your tomcat server: 
`war/server-2.5.1.war`

### interfaces
the following REST methods are provided:
* Account management
    * (POST) /account/create : creates a new account. The first account ever created is an admin
        * request body
            * email: String (not null, unique)
            * password: String (not null, 8-20 chars, at least two different kind of characters)
            * address: String
            * phoneNumber: String (prefix 0 or +41 followed by 9 digits, whitespaces allowed)
    * (PUT) /account/update : updates an account 
        * request body
            * (optional) email: String (not null, unique)
            * (optional) password: String (not null, 8-20 chars, at least two different kind of characters)
            * (optional) address: String
            * (optional) phoneNumber: String (prefix 0 or +41 followed by 9 digits, whitespaces allowed)
            * (optional) clearanceLevel: Integer (only remote allowed, cannot degrade an admin account)
        * headers
            * id: Long (account id of the requester)
            * token: String (token corresponding to id)
        * params
            * (optional) remote: Long (account id to update, only possible as admin)
    * (DELETE) /account/delete : deletes an account. cannot delete an admin account
        * headers
            * id: Long (account id of the requester)
            * token: String (token corresponding to id)
        * params
            * (optional) remote: Long (account id to delete, requires clearance level >= 3)
    * (GET) /account/get : fetches an account
        * headers
            * id: Long (account id of the requester)
            * token: String (token corresponding to id)
        * params
            * (optional) remote: Long (account id to fetch, requires clearance level >= 3)
    * (GET) /account/login : fetches an account by email and password, refreshes the token
        * headers
            * email: String (email of a registered account)
            * password: String (password correcponding to email)
* Assortment Management
    * (GET) /ingredients/all : fetches all ingredients
        * params
            * (optional) available: Boolean (filters available ingredients)
    * (POST) /ingredients/create : creates a new ingredient
        * request body
            * name: String (not null, unique)
            * desc: String
            * (optional) available: Boolean
        * headers
            * accountId: Long (account id of the requester, must have clearance level >= 2)
            * token: String (token corresponding to accountId)
    * (PUT) /ingredients/update : updates an ingredient
        * request body
            * (optional) name: String (not null, unique)
            * (optional) desc: String
            * (optional) available: Boolean
        * headers
            * accountId: Long (account id of the requester, must have clearance level >= 2)
            * token: String (token corresponding to accountId)
            * ingredientId: Long (id of the ingredient to update)
    * (GET) /pizzas/all : fetches all pizzas
        * params
            * (optional) available: Boolean (filters available pizzas)
    * (POST) /pizzas/create : creates a new pizza
        * request body
            * name: String (not null, unique)
            * desc: String
            * ingredientIds: List<Long> (must refer to ingredient ids)
            * price: Integer (>= 0)
            * (optional) available: Boolean
        * headers
            * accountId: Long (account id of the requester, must have clearance level >= 2)
            * token: String (token corresponding to accountId)
    * (PUT) /pizzas/update : updates a pizza
        * request body
            * (optional) name: String (not null, unique)
            * (optional) desc: String
            * (optional) ingredientIds: List<Long> (must refer to ingredient ids)
            * (optional) price: Integer (>= 0)
            * (optional) available: Boolean
        * headers
            * accountId: Long (account id of the requester, must have clearance level >= 2)
            * token: String (token corresponding to accountId)
            * pizzaId: Long (id of the pizza to update)
* Order Management
    * (POST) /orders/create : creates a new order
        * request body
            * pizzaIds: List<Long> (must refer to pizzas)
            * (optional) comment: String
        * headers
            * accountId: Long (account id of the requester)
            * token: String (token corresponding to accountId)
    * (GET) /orders/get : fetches a specific order
        * headers
            * accountId: Long (account id of the requester, must be owner of order or have clearance level >= 1)
            * token: String (token corresponding to accountId)
            * orderId: Long (id of the order to fetch)
    * (GET) /orders/all : fetches a list of orders
        * headers
            * accountId: Long (account id of the requester, must have clearance level >= 1)
            * token: String (token corresponding to accountId)
        * params
    * (PUT) /orders/update : updates an order
        * request body
            * status: OrderStatus (CREATED, CONFIRMED, READY or DELIVERED)
            * (optional) comment: String
        * headers
            * accountId: Long (account id of the requester, must have clearance level >= 1)
            * token: String (token corresponding to accountId)
            * orderId: Long (id of the order to update)
