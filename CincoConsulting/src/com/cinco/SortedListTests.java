package com.cinco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class SortedListTests {

	@Test
	public void testAddMaintainsComparatorOrder() {
		SortedList<Integer> numbers = new SortedList<>((a, b) -> a.compareTo(b));

		numbers.add(4);
		numbers.add(1);
		numbers.add(3);
		numbers.add(2);

		assertEquals(4, numbers.size());
		assertEquals(1, numbers.get(0));
		assertEquals(2, numbers.get(1));
		assertEquals(3, numbers.get(2));
		assertEquals(4, numbers.get(3));
	}

	@Test
	public void testRemoveShiftsElements() {
		SortedList<Integer> numbers = new SortedList<>((a, b) -> a.compareTo(b));

		numbers.add(3);
		numbers.add(1);
		numbers.add(2);

		assertEquals(2, numbers.remove(1));
		assertEquals(2, numbers.size());
		assertEquals(1, numbers.get(0));
		assertEquals(3, numbers.get(1));
	}

	@Test
	public void testInvoiceTotalTieBreaksByUuid() {
		Address address = new Address("1400 R St", "Lincoln", "NE", "68588");
		Person person = new Person(UUID.fromString("11111111-2222-3333-4444-555555555555"), "Barron", "Vaughn", "911",
				new java.util.ArrayList<>());
		Company company = new Company(UUID.fromString("66666666-7777-8888-9999-000000000000"), person, "CCC Client",
				address);

		Invoice first = new Invoice(UUID.fromString("00000000-0000-0000-0000-000000000001"), company, person,
				java.time.LocalDate.now(), new java.util.ArrayList<>());
		Invoice second = new Invoice(UUID.fromString("00000000-0000-0000-0000-000000000002"), company, person,
				java.time.LocalDate.now(), new java.util.ArrayList<>());

		SortedList<Invoice> invoices = new SortedList<>((a, b) -> {
			int totalComparison = Double.compare(b.getGrandTotal(), a.getGrandTotal());
			if (totalComparison != 0) {
				return totalComparison;
			}
			return a.getInvoiceUuid().compareTo(b.getInvoiceUuid());
		});

		invoices.add(second);
		invoices.add(first);

		assertEquals(first, invoices.get(0));
		assertEquals(second, invoices.get(1));
	}

	@Test
	public void testInvalidIndexThrowsException() {
		SortedList<Integer> numbers = new SortedList<>((a, b) -> a.compareTo(b));

		assertThrows(IndexOutOfBoundsException.class, () -> numbers.get(0));
	}
}
