package com.tim.IOT_AVR.Service

import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import java.util.Scanner

import com.fazecast.jSerialComm.{SerialPort, SerialPortDataListener, SerialPortEvent}
import com.tim.IOT_AVR.AppStartRunner
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import scala.beans.BeanProperty


@Service
class AVRService() {


  private val LOG = LoggerFactory.getLogger(classOf[AVRService])



  val sp: SerialPort = SerialPort.getCommPort("COM3")               // device name
  sp.setComPortParameters (9600, 8, 0, 0) // connection settings to the Atmega328p.
  sp.setComPortTimeouts (SerialPort.TIMEOUT_SCANNER, 0, 0)


    //  Open port and returns true if success
    def openPort(): Boolean = {

      return sp.openPort()
    }

    def writeByteToSerialPort(bufferByte: Array[Byte], bytesToWrite: Long): Unit = {

      Thread.sleep(500)
      sp.writeBytes(bufferByte, bytesToWrite)
    }

    //  Write to serial port
    def writeSerialPort(output: String): Unit ={

      Thread.sleep(500)
      val out = new PrintWriter(sp.getOutputStream, true)
      out.println(output)
    }

}
