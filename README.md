# Spring + Atmega328p
In this project you can control the Atmega328p/Arduino uno microcontroller through Spring boot restful microservices.
Once you set it up on the internet you can control it from anywhere in the world with a internet connection.

Which is easily done in the spring framework, cause the web server is already in a container in the java JAR.
So you pretty much build the spring project JAR and fire it up and it will be working by default on localhost:8080, if port 8080 is free though.

Purely to show Spring IOT capabilities and to document on how easily it is done.

It's developed with serial communication through a USB using JSerialComm library its platform independent so you can use it on
Windows / Linux / Mac, default is "COM3" but you may have to change its USB port adress to your OS specifications Win: "COM0", Linux: "dev/ttyUSB0" for example or if you use another port address number where the microcontroller is connected to.

Made in Scala with spring boot and the Atmega328p microcontroller in embedded-C with avrdude.


!WARNING i recon you know some basic electronics, consult some schemas on how to connect LED's or a potentiometer on the PortC0 for example there are dozens examples on how to do it don't blow your pins up.

Work is still in progress for extra features these are ready to use.

1. By pressing 1 for LED on and 0 for LED off in console. 
   - Connect your LED on PortB pin 0 or pin 8 on the Arduino Uno

2. Read analog data from 0 to 255 
   - Its on de default pin PortC 0 or pin A0 on the Arduino Uno
