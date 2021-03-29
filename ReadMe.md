# On using Java to generate assemblycode for microcontrollers.

The way it is done is stright forward - easy_to_understand-methods i Java holds a number of not_so_easy_to_understand-lines of assemblycode. When the program is run, and a method is called, it will add its assemblylines to the assemblyprogram. 

The coding is object-oriented in that each microcontroller register is contained within an object. The object also contains methods to manipulate the content of the contained registers. In this way the coding is done with Java-objects, while assemblycode is generated when run.
