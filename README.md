# **Cupon:**

La app esta construida en java 8 con springboot, se esta ejecutando en azure como un app service por lo que puede escalar tanto vertical como horizontalmente. Para los casos de prueba use newman con una coleccion de postman.

Para ejecutar el programa de forma local se usa el siguiente comando de maven

    mvn spring-boot:run


### **Ejecucion del programa**

**Swagger:**

Ejecucion local:
    
   http://localhost:8080/swagger-ui.html#/coupon-controller/getItemsUsingPOST
   
Ejecucion desde la nube:
 
   https://test-meli-coupon.azurewebsites.net/swagger-ui.html#/coupon-controller/getItemsUsingPOST
    
    body:
        {
          "amount": 13500,
          "item_ids": [
            "MLA895941129",
            "MLA895941130",
            "MLA895941131",
            "MLA895941132",
            "MLA895941133",
            "MLA895941130",
            "MLA895941134",
            "MLA895941135",
            "MLA895941136",
            "MLA895941137",
            "MLA895941138",
            "MLA895941139",
            "MLA895941140",
            "MLA895941141",
            "MLA895941142",
            "MLA895941143",
            "MLA895941144",
            "MLA895941145",
            "MLA895941146",
            "MLA895941147",
            "MLA895941148",
            "MLA895941149",
            "MLA895941150",
            "MLA895941151"
          ]
        }
        

**cURL:** 

Ejecucion local:

    curl -X POST "http://localhost:8080/api/v1/coupon" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"amount\": 13500, \"item_ids\": [ \"MLA895941129\", \"MLA895941130\", \"MLA895941131\", \"MLA895941132\", \"MLA895941133\", \"MLA895941130\", \"MLA895941134\", \"MLA895941135\", \"MLA895941136\", \"MLA895941137\", \"MLA895941138\", \"MLA895941139\", \"MLA895941140\", \"MLA895941141\", \"MLA895941142\", \"MLA895941143\", \"MLA895941144\", \"MLA895941145\", \"MLA895941146\", \"MLA895941147\", \"MLA895941148\", \"MLA895941149\", \"MLA895941150\", \"MLA895941151\" ]}"

Ejecucion desde la nube:

    curl -X POST "https://test-meli-coupon.azurewebsites.net/api/v1/coupon" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"amount\": 13500, \"item_ids\": [ \"MLA895941129\", \"MLA895941130\", \"MLA895941131\", \"MLA895941132\", \"MLA895941133\", \"MLA895941130\", \"MLA895941134\", \"MLA895941135\", \"MLA895941136\", \"MLA895941137\", \"MLA895941138\", \"MLA895941139\", \"MLA895941140\", \"MLA895941141\", \"MLA895941142\", \"MLA895941143\", \"MLA895941144\", \"MLA895941145\", \"MLA895941146\", \"MLA895941147\", \"MLA895941148\", \"MLA895941149\", \"MLA895941150\", \"MLA895941151\" ]}"
    

**Ejemplo de respuesta exitosa:** 

    {
      "item_ids": [
        "MLA895941151",
        "MLA895941149",
        "MLA895941140",
        "MLA895941134",
        "MLA895941133",
        "MLA895941132",
        "MLA895941135"
      ],
      "total": 13500
    }
    
    
 **Casos de prueba:** 
 
 Resultado de la ejecucion de casos de prueba
 
    newman run -n 100 ../coupons/Meli-Coupon.postman_collection.json     
 
    ┌─────────────────────────┬────────────────────┬────────────────────┐
    │                         │           executed │             failed │
    ├─────────────────────────┼────────────────────┼────────────────────┤
    │              iterations │                100 │                  0 │
    ├─────────────────────────┼────────────────────┼────────────────────┤
    │                requests │                100 │                  0 │
    ├─────────────────────────┼────────────────────┼────────────────────┤
    │            test-scripts │                100 │                  0 │
    ├─────────────────────────┼────────────────────┼────────────────────┤
    │      prerequest-scripts │                200 │                  0 │
    ├─────────────────────────┼────────────────────┼────────────────────┤
    │              assertions │                300 │                  0 │
    ├─────────────────────────┴────────────────────┴────────────────────┤
    │ total run duration: 27.8s                                         │
    ├───────────────────────────────────────────────────────────────────┤
    │ total data received: 4.39KB (approx)                              │
    ├───────────────────────────────────────────────────────────────────┤
    │ average response time: 214ms [min: 185ms, max: 858ms, s.d.: 65ms] │
    └───────────────────────────────────────────────────────────────────┘


