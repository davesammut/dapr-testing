##DAPR Demo

A Java Springboot DAPR (Event-driven, portable runtime for building microservices on cloud and edge) demo. 

The example demonstrates a controller listening to a dapr pub-sub topic (topic-addition) for 
messages to perform an addition calculation of the operands in the event.

###HomeController

Configured with daprPublishUrl (http://localhost:%s/v1.0/publish/topic-addition)

#### Test http endoints

/ GET
  Return a HelloWorld ("Hello Mister Calculator") String response
  
/calculation-result GET
  Return the singleton calculation result as a String response. Used to return the result of the dapr event calculation. 

/add POST
  Take a JSON body of two operands and return addition response as String. Used to test the calculator.

/topic-addition-publish-trigger POST
  Take a JSON body of two operands and publish JSON to daprPublishUrl. Used to simulate a dapr event publication.

####DAPR pub-sub methods

/dapr/subscribe GET
  Return JSON document containing list of subscribed DAPR pub-sub topics (topic-addition)
  
/topic-addition POST
  Read the JSON body as a DaprMessage representing the two operands, perform the addition and set the singleton calculation result.
  
 
 
 ###Setting up
 
 1. Install DAPR
 
 `wget -q https://raw.githubusercontent.com/dapr/cli/master/install/install.sh -O - | /bin/bash`
 
 `sudo dapr init`
 
 
 2. Build the Java
 
 `mvn clean install` 
 
 ###Running locally

`dapr run --app-id addcalculator --app-port 8080 --port 3500 -- mvn spring-boot:start`
 
 Test dapr is running the app
 
`dapr send --app-id addcalculator --method /` 
 
####Test pub-sub

use dapr to publish an additional event

`dapr publish --topic topic-addition --payload '{"operand1":1,"operand2":2}'`

should output

`Event published successfully `

use dapr to route a test http message to return the result of the event

`dapr send --app-id addcalculator --method /calculation-result`

should output 

`3`


