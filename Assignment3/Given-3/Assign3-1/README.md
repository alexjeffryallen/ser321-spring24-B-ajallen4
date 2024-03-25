##### Author: Instructor team SE, ASU Polytechnic, CIDSE, SE


##### Purpose
This program shows a very simple client server implementation. The server
has 3 services, echo, add, addmany. Basic error handling on the server side
is implemented. Client does not have error handling and only has hard coded
calls to the server.

* Please run `gradle Server` and `gradle Client` together.
* Program runs on localhost
* Port is hard coded

## Protocol: ##

### Echo: ###

Request: 

    {
        "type" : "echo", -- type of request
        "data" : <String>  -- String to be echoed 
    }

General response:

    {
        "type" : "echo", -- echoes the initial response
        "ok" : <bool>, -- true or false depending on request
        "echo" : <String>,  -- echoed String if ok true
        "message" : <String>,  -- error message if ok false
    }

Success response:

    {
        "type" : "echo",
        "ok" : true,
        "echo" : <String> -- the echoed string
    }

Error response:

    {
        "type" : "echo",
        "ok" : false,
        "message" : <String> -- what went wrong
    }

### Add: ### 
Request:

    {
        "type" : "add",
        "num1" : <String>, -- first number -- String needs to be an int number e.g. "3"
        "num2" : <String> -- second number -- String needs to be an int number e.g. "4" 
    }

General response

    {
        "type" : "add", -- echoes the initial request
        "ok" : <bool>, -- true or false depending on request
        "result" : <int>,  -- result if ok true
        "message" : <String>,  -- error message if ok false
    }

Success response:

    {
        "type" : "add",
        "ok" : true,
        "result" : <int> -- the result of add
    }

Error response:

    {
        "type" : "add",
        "ok" : false,
        "message" : <String> - error message about what went wrong
    }

### AddMany: ###
Another request, this one does not just get two numbers but gets an array of numbers.

Request:

    {
        "type" : "addmany",
        "nums" : [<String>], -- json array of ints but given as Strings, e.g. ["1", "2"]
    }

General response

    {
        "type" : "addmany", -- echoes the initial request
        "ok" : <bool>, -- true or false depending on request
        "result" : <int>,  -- result if ok true
        "message" : <String>,  -- error message if ok false
    }

Success response:

    {
        "type" : "addmany",
        "ok" : true,
        "result" : <int> -- the result of adding
    }

Error response:

    {
        "type" : "addmany",
        "ok" : false,
        "message" : <String> - error message about what went wrong
    }

### Roller: ###
This one is a simple die roller. "dieCount" represents the number of die that the user wants to roll, "faces" represents how many faces the die has (so we are not just limiting it to 6). The server will roll the die and return which number came up how often. 

Request:

    {
        "type" : "roller",
        "dieCount" : <int>,
        "faces" : <int>
    }

General response

    {
        "type" : "roller", -- echoes the initial request
        "ok" : <bool>, -- true or false depending on request
        "result" : {<int>:<int>, <int>:<int>, <int>:<int>},  -- result if ok true - returns a JSon object with the key being the number on the face and the value how often it came up
        "message" : <String>  -- error message if ok false
    }

Success response:

    {
        "type" : "roller",
        "ok" : true,
        "result" : {<int>:<int>, <int>:<int>, <int>:<int>} -- JSon object with key being face number and value being number of times it came up
    }

Success response example for a dieCount = 5 and faces=6:

    {
        "type" : "roller",
        "ok" : true,
        "result" : {1:1, 2:2, 4:1, 5:1} -- including only the numbers that were rolled
    }

Error response:

    {
        "type" : "roller",
        "ok" : false,
        "message" : <String> -- error message about what went wrong
    }


### Inventory: ###
This one will be to add, view or take something from an inventory. 

The server stores a list of products and their quantity and through the client one can add a new product with a quantity. Stock up on the items by using providing a product that is already on the list. It is also possible to just view the inventory or buy something. 
If you want to do it well, then store the inventory list persistently (not required).

Request to add to the inventory:

    {
        "type" : "inventory",
        "task": "add",
        "productName" : <String>,
        "quantity" : <int>
    }

Request to view the inventory:

    {
        "type" : "inventory",
        "task" : "view"
    }

Request to buy from the inventory:

    {
        "type" : "inventory",
        "task" : "buy",
        "productName" : <String>,
        "quantity" : <int>
    }

General response

    {
        "type" : "inventory", -- echoes the initial request
        "ok" : <bool>, -- true or false depending on request
        "inventory" : [{"product": <String>, "quantity": int}],  -- result if ok true - returns the current inventory as list of JSon objects
        "message" : <String>  -- error message if ok false
    }

Success response:

    {
        "type" : "inventory",
        "ok" : true,
        "inventory" : [{"product": <String>, "quantity": int}],  --  e.g. [{"product": "Road bike", "quantity": 5},   {"product": "helmet", "quantity": 10}]
    }


Error response:

    {
        "type" : "inventory",
        "ok" : false,
        "message" : <String> -- error message about what went wrong
    }

Error response for buying a product that is not available in quantity:

    {
        "type" : "inventory",
        "ok" : false,
        "message" : Product <X> not available in quantity <Y>
    }

Error response for buying a product that is not in inventory at all:

    {
        "type" : "inventory",
        "ok" : false,
        "message" : Product <X> not in inventory
    }
### General error responses: ###
These are used for all requests.

Error response: When a required field "key" is not in request

    {
        "ok" : false
        "message" : "Field <key> does not exist in request" 
    }

Error response: When a required field "key" is not of correct "type"

    {
        "ok" : false
        "message" : "Field <key> needs to be of type: <type>"
    }

Error response: When the "type" is not supported, so an unsupported request

    {
        "ok" : false
        "message" : "Type <type> is not supported."
    }


Error response: When the "type" is not supported, so an unsupported request

    {
        "ok" : false
        "message" : "req not JSON"
    }