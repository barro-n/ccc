Authors:
Barron Vaughn - bvaughn5@huskers.unl.edu - NUID: 28693502
Lewis Rokke - lrokke2@huskers.unl.edu - NUID: 79613756

This is the phase one implementation of the Cinco Computer Consultants 
infrastructure development project. This phase focuses on Electronic Data Interchange,
allowing CSV files to be input and output as both XML and JSON files.

This phase contains the following classes:

* DataConverter.Java - Handles the actual parsing and conversion
* Address.java
* Company.java
* Invoice.java
* Person.java
* Item.java
	* Equipment.java (extends Item)
	* License.java	 (extends Item)
	* Service.java	 (extends Item)
	
This project requires the following dependencies:
XStream - for XML conversion
Gson - for JSON conversion

