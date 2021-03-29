# On using Java to generate assemblycode for microcontrollers.

The way it is done is stright forward - easy_to_understand-methods i Java holds a number of not_so_easy_to_understand-lines of assemblycode. When the program is run, and a method is called, it will add its assemblylines to the assemblyprogram. 

The coding is object-oriented in that each microcontroller register is contained within an object. The object also contains methods to manipulate the content of the contained registers. In this way the coding is done simply by working in Java.

When programming this way the logic is easier to understand, the units are easy to setup and there will be less code.

## Microcontroller
It is the 16F690 microcontroller from Microchip that is used. 

#### Units
Code has so far been made to set up these units: 
- Clock
- Pins
- Adc
- Timer0
- Interrupts

#### Methods
A number of methods has been implemented with different types of input. E.g. add, sub, AND, OR, etc

#### Logic
Some methods for logic comparison, like equals, isBiggerThan has been implemented. 

#### Interrupt
The interruptroutine can be comletely coded in Java.

#### Subroutines
Subroutines have been made and is available at need
- DIVIDE 3 kinds, with various 8 and 16bit in- and output registers.
- MULTIPLY 
---
