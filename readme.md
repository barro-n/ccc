
**CINCO COMPUTER CONSULTANTS - INTERNAL INFRASTRUCTURE OVERHAUL PROJECT**

Authors:<br>
Barron Vaughn - bvaughn5@huskers.unl.edu - NUID: 28693502<br>
Lewis Rokke - lrokke2@huskers.unl.edu - NUID: 79613756<br>

This project contains the Phase 1, 2, 3, 4, 5, 6, and 7 implementations of the Cinco Computer
Consultants development project.

	* Phase 1 - Electronic Data Interchange
	* Phase 2 - Class Hierarchy Design and Testing
	* Phase 3 - Invoice System and Report Generation
	* Phase 4 - Database Design & Implementation
	* Phase 5 - Object Instantiation and Database Persistence
	* Phase 6 - Database Interaction API
	* Phase 7 - Sorted List ADT

These phases contain the following classes:

Utilities & Drivers:

	* DataConverter.java - Main driver for EDI 
	* DataLoader.java - Parses CSV files into Java objects
	* DataPersister.java - Serializes objects into XML and JSON
	* DatabaseLoader.java - Handles SQL retrieval and object mapping
	* ConnectionFactory.java - Establishes and manages the JDBC connection to the database
	* InvoiceReport.java - Main Driver for reporting
	* InvoiceData.java - Suite of methods for interacting with the database
	* SortedList.java - Generic sorted list ADT

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
	* SortedListTests.java - Unit tests for the sorted list
	
<br>
This project requires the following dependencies:

	* XStream - for XML conversion
	* MySQL-Connector - for database connectivity
	* Gson - for JSON conversion
	* JUnit - for testing
	* Log4j - for logging
<br>

_A note on documentation:_

All classes and nontrivial methods have been given doc-style documentation, briefly describing 
their functionality and application. Some getter functions, which would otherwise be considered
trivial, have been given documentation as they contain important calculation functionality.



