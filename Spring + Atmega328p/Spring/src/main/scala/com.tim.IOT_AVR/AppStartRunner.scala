/*
 *  Created by Tim Vandenbroecke 4/06/2019
 *
 *  - This software is free to use as you wish
 */





package com.tim.IOT_AVR

import java.io.{BufferedReader, IOException, InputStreamReader, PrintWriter}
import java.util.Scanner

import com.fazecast.jSerialComm.{SerialPort, SerialPortDataListener, SerialPortEvent}
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.{ApplicationArguments, ApplicationRunner}
import org.springframework.stereotype.Component


@Component
class AppStartRunner extends ApplicationRunner {

  private val LOG = LoggerFactory.getLogger(classOf[AppStartRunner])

  @throws(classOf[Exception])
  @throws(classOf[IOException])
  @throws(classOf[InterruptedException])
  @throws(classOf[IllegalStateException])
  def run(args: ApplicationArguments): Unit   =  {

    val portAddress: String = "COM3" //Ports address of the connected USB.  TODO: must be changed if you use another port or use Linux/Mac OS
    val BaudRate = 9600 //BAUD rate of the microcontroller needs to be the same here
    val newDataBits = 8 //Bit width of the serial communication specified by the UCSZ bit of the UCSRC register in the Atmega328p.
    val newStopBits = 1 //Stop bit specified by the USBS register in the Atmega328p.
    val newParity = 0   //Parity bit specified by the UPM register in the Atmega328p , none, 1 bit or 2 bit parity check.

    val sp: SerialPort = SerialPort.getCommPort (portAddress)               // device name
    sp.setComPortParameters (BaudRate, newDataBits, newStopBits, newParity) // connection settings to the Atmega328p.
    sp.setComPortTimeouts (SerialPort.TIMEOUT_READ_BLOCKING, 200, 0)
    sp.setComPortTimeouts (SerialPort.TIMEOUT_SCANNER, 0, 0)

    if(sp.openPort){

      System.out.println("\n\n Connected to device success!" )
    }else{

      System.out.println("\n\n Opening port failed!\n Either your device is not connected.\n Or change portAddress in AppStartRunner.scala to the right port address!")

    }

    System.out.println("\n\n\n\nPress 1 for LED on \nPress 0 for LED off" )

    val reader = new BufferedReader(new InputStreamReader(System.in))
    val out = new PrintWriter(sp.getOutputStream, true)

    while(true) {
      try {

        val name = reader.readLine
        out.println(name)

      }catch{

        case e: NullPointerException => println(e)
      }

      //Listens to incoming data in a loop
      sp.addDataListener(new SerialPortDataListener() {

        override def getListeningEvents: Int = SerialPort.LISTENING_EVENT_DATA_AVAILABLE

        override def serialEvent(serialPortEvent: SerialPortEvent): Unit = {

          var input = ""
          val in = new Scanner(sp.getInputStream)
          input = in.nextLine
          System.out.println("Microcontroller return message: " + input)
          in.close()

        }
      })
    }
  }
}
