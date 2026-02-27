
**CINCO COMPUTER CONSULTANTS - INTERNAL INFRASTRUCTURE OVERHAUL PROJECT**

Authors:<br>
Barron Vaughn - bvaughn5@huskers.unl.edu - NUID: 28693502<br>
Lewis Rokke - lrokke2@huskers.unl.edu - NUID: 79613756<br>

This project contains the Phase 1 & 2 implementations of the Cinco Computer
Consultants development project.
	Phase 1 - Electronic Data Interchange
	Phase 2 - Class Hierarchy Design and Testing

These phases contain the following classes:

Utilities:

	* DataConverter.java - Main driver for EDI 
	* DataLoader.java - Parses CSV files into Java objects
	* DataPersister.java - Serializes objects into XML and JSON formats

General Entities:

	* Item.java (Abstract) - Base class for all general items
	* Equipment.java (Extends Item)
	* License.java (Extends Item)
	* Service.java (Extends Item)
	* Person.java
	* Company.java
	* Address.java

Invoice & Billing Entities:

	* Invoice.java - Collects line items and calculates totals
	* InvoiceItem.java (Interface) - Defines standards for cost and tax calculations
	* Purchase.java (Implements InvoiceItem)
	* Lease.java (Implements InvoiceItem)
	* BilledService.java (Implements InvoiceItem)
	* BilledLicense.java (Implements InvoiceItem)
	
Testing Classes:

	* EntityTests.java - Validates cost and tax logic for individual billing configurations.
	* InvoiceTests.java - Validates aggregation and total calculations for full invoices.
	

This project requires the following dependencies:<br>

	* XStream - for XML conversion
	* Gson - for JSON conversion
	* JUnit - for testing
<br>

_A note on documentation:_

All classes and nontrivial methods have been given doc-style documentation, briefly describing 
their functionality and application. Some getter functions, which would otherwise be considered
 trivial, have been given documentation as they contain important calculation functionality.



