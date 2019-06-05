package com.tim.IOT_AVR.controller

import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
class AVRController {


  @RequestMapping(value = Array("/led"), method = Array(RequestMethod.POST))
   def leds(): Unit = {


  }
}
