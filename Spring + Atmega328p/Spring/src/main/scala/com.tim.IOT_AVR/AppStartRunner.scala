/*
 *  Created by Tim Vandenbroecke 4/06/2019
 *
 *  - This software is free to use as you wish
 */





package com.tim.IOT_AVR

import java.io.{BufferedReader, IOException, InputStreamReader, PrintWriter}
import java.util.Scanner

import com.fazecast.jSerialComm.{SerialPort, SerialPortDataListener, SerialPortEvent}
import com.tim.IOT_AVR.Service.AVRService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.{ApplicationArguments, ApplicationRunner}
import org.springframework.stereotype.Component


@Component
class AppStartRunner extends ApplicationRunner {


  private val LOG = LoggerFactory.getLogger(classOf[AppStartRunner])

  //  Initialize the AVR serial communication service
  @Autowired
  val serialPort: AVRService = new AVRService()


  @throws(classOf[Exception])
  @throws(classOf[IOException])
  @throws(classOf[InterruptedException])
  @throws(classOf[IllegalStateException])
  def run(args: ApplicationArguments): Unit   =  {


    if(serialPort.openPort){

      System.out.println("\n\n Connected to device success!" )
    }else{

      System.out.println("\n\n Opening port failed!\n Either your device is not connected.\n Or change portAddress in AppStartRunner.scala to the right port address!")

    }

    System.out.println("\n\n1: Turn on LED's\n" +
                            "2: Read analog ADC\n")

    val reader = new BufferedReader(new InputStreamReader(System.in))
    val select = reader.readLine

    while(true) {



      select match {

        case "1" => System.out.println("Press 1 for LED on\nPress 0 for LED off\n")
          val ledReader = new BufferedReader(new InputStreamReader(System.in))
          val selectLed = ledReader.readLine
          if(selectLed == "1"){

            try {
              //val out = new PrintWriter(sp.getOutputStream, true)
              //out.println("1")
              serialPort.writeSerialPort("1")

              serialPort.sp.addDataListener(new SerialPortDataListener() {

                override def getListeningEvents: Int = SerialPort.LISTENING_EVENT_DATA_AVAILABLE

                override def serialEvent(serialPortEvent: SerialPortEvent): Unit = {

                  var input = ""
                  val in = new Scanner(serialPort.sp.getInputStream)

                  input = in.nextLine
                  System.out.println("Microcontroller replies: " + input)

                  in.close()

                }
              });

            }catch{

              case e: NullPointerException => println(e)
            }
          }else if(selectLed == "0"){
            try {

              serialPort.writeSerialPort("0")
              serialPort.sp.addDataListener(new SerialPortDataListener() {

                override def getListeningEvents: Int = SerialPort.LISTENING_EVENT_DATA_AVAILABLE

                override def serialEvent(serialPortEvent: SerialPortEvent): Unit = {

                  var input = ""
                  val in = new Scanner(serialPort.sp.getInputStream)

                  input = in.nextLine
                  System.out.println("Microcontroller replies: " + input)

                  in.close()

                }
              });
            }catch{

              case e: NullPointerException => println(e)
            }
          }




      case "2" => System.out.println(" Press 1 to receive analog data from PORTC0 or A0\n")
        val ledReader = new BufferedReader(new InputStreamReader(System.in))
        val selectLed = ledReader.readLine
        if(selectLed == "1"){

          try {

            serialPort.writeSerialPort("3")
            serialPort.sp.addDataListener(new SerialPortDataListener() {

              override def getListeningEvents: Int = SerialPort.LISTENING_EVENT_DATA_AVAILABLE

              override def serialEvent(serialPortEvent: SerialPortEvent): Unit = {

                var input = ""
                val in = new Scanner(serialPort.sp.getInputStream)

                input = in.nextLine
                System.out.println("Microcontroller ADC value: " + input)

                in.close()

              }
            });

          }catch{

            case e: NullPointerException => println(e)
          }
        }
      }
    }
  }
}


object AppStartRunner{


}