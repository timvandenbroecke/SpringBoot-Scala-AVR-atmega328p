/* 
 * File:   main.c
 * Author: Tim Vandenbroecke
 *
 * Created on 2 juni 2019, 12:29
 */

#define F_CPU 16000000  //AVR CPU speed 16Mhz
#define BAUD 9600   //Baud rate we are using
#define BRC ((F_CPU / BAUD / 16) - 1)   //Baud rate calculator

#include <avr/io.h>
#include <util/delay.h>
#include <stdlib.h>
#include <avr/interrupt.h>
#include <string.h>

//  Set bits macro's to toggle leds
#define cbi(sfr, bit) (_SFR_BYTE(sfr) &= ~_BV(bit))
#define sbi(sfr, bit) (_SFR_BYTE(sfr) |= _BV(bit))

#define TX_BUFFER_SIZE 128  //Transmission buffer 
#define RX_BUFFER_SIZE 128  //Read buffer

char serialBuffer[TX_BUFFER_SIZE];
uint8_t serialReadPos = 0;
uint8_t serialWritePos = 0;

char rxBuffer[RX_BUFFER_SIZE];
uint8_t rxReadPos = 0;
uint8_t rxWritePos = 0;

// Write
void appendSerial(char c);
void serialWrite(char c[]);

//  Read
char getChar(void);
char peekChar(void);

int main(void) {
    
    UBRR0H = (BRC >> 8);    //Upper register
    UBRR0L = BRC;   //Lower register
    
    //  TX & RX
    UCSR0B = (1 << TXEN0) | (1 << TXCIE0) | (1 << RXEN0) | (1 << RXCIE0);  //Setup control register B, transmitter and read enable
    UCSR0C = (1 << UCSZ01) | (1 << UCSZ00);  //Set up the 8bit data frame
    

      
    DDRB = (1 << PORTB0);
    
    sei();  //Enable interupts
    
    while(1){   //Infinite loop
        
        char c = getChar();
        
        if(c == '1'){
            
            sbi(PORTB, PORTB0);
        }else if(c == '0'){
            
            cbi(PORTB, PORTB0);
        }
    }
    
    return 0;
}

void appendSerial(char c){
    
    serialBuffer[serialWritePos] = c;
    serialWritePos++;
    
    if(serialWritePos >= TX_BUFFER_SIZE){
        
        serialWritePos = 0;
    }
}

void serialWrite(char c[]){
    
    for(uint8_t i = 0; i < strlen(c); i++){
        
        appendSerial(c[i]);
    }
    
    if(UCSR0A & (1 << UDRE0)){
        
        UDR0 = 0;
    }
}

ISR(USART_TX_vect){ //This will fire up every time data is transmitted
    
    if(serialReadPos != serialWritePos){
        
        UDR0 = serialBuffer[serialReadPos];
        serialReadPos++;
        
        if(serialReadPos >= TX_BUFFER_SIZE){
            
            serialReadPos++;
        }
    }
}

char peekChar(void){
    
    char ret = '\0';
    
    if(rxReadPos != rxWritePos){
        
        ret = rxBuffer[rxReadPos];
    }
    
    return ret;
}   

char getChar(void){
    
    char ret = '\0';
    
    if(rxReadPos != rxWritePos){
        
        ret = rxBuffer[rxReadPos];
        rxReadPos++;
        
        if(rxReadPos >= RX_BUFFER_SIZE){
            
            rxReadPos = 0;
        }
    }
    
    return ret;
}

ISR(USART_RX_vect){ //This will fire up every time data is received
    
    rxBuffer[rxWritePos] = UDR0;
    
    rxWritePos++;
    
    if(rxWritePos >= RX_BUFFER_SIZE){
        
        rxWritePos = 0;
    }
}