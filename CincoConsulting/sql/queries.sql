-- 1
select person.person_uuid, person.first_name, person.last_name 
from person;

-- 2
select person.person_uuid, person.first_name, person.last_name, person.phone, email.email_address 
from person 
left join email on person.person_id = email.person_id;

-- 3 
select email.email_address 
from email 
join person on email.person_id = person.person_id 
where person.person_uuid = '11111111-1111-1111-1111-111111111111';

-- 4
update email 
set email_address = 'barron.vaughn@nsa.gov' 
where email_id = 1;

-- 5
delete from email 
where person_id = (select person_id from person where person_uuid = '44444444-4444-4444-4444-444444444444');
update invoice_item set consultant_id = null 
where consultant_id = (select person_id from person where person_uuid = '44444444-4444-4444-4444-444444444444');
delete from person 
where person_uuid = '44444444-4444-4444-4444-444444444444';

-- 6
select item.name, invoice_item.type, invoice_item.quantity, invoice_item.billed_hours, invoice_item.term_in_months 
from invoice_item, item, invoice 
where invoice_item.item_id = item.item_id 
and invoice_item.invoice_id = invoice.invoice_id 
and invoice.invoice_uuid = 'dddddddd-dddd-dddd-dddd-dddddddddddd';

-- 7
select item.name, invoice_item.type, invoice.invoice_date 
from invoice_item 
join invoice on invoice_item.invoice_id = invoice.invoice_id 
join company on invoice.customer_id = company.company_id 
join item on invoice_item.item_id = item.item_id 
where company.company_uuid = '66666666-6666-6666-6666-666666666666';

-- 8 
select company.name, count(invoice.invoice_id) as total_invoices 
from company 
left join invoice on company.company_id = invoice.customer_id 
group by company.company_id;

-- 9
select person.first_name, person.last_name, count(invoice.invoice_id) as total_sales 
from person 
join invoice on person.person_id = invoice.salesperson_id 
group by person.person_id 
having count(invoice.invoice_id) > 0;

-- 10
select invoice.invoice_uuid, sum(invoice_item.quantity * item.cost_per_unit) as equipment_subtotal 
from invoice_item 
join item on invoice_item.item_id = item.item_id 
join invoice on invoice_item.invoice_id = invoice.invoice_id 
where item.type = 'E' and invoice_item.type = 'Purchase' 
group by invoice.invoice_id;

-- 11
select invoice.invoice_uuid, item.name, count(invoice_item.invoice_item_id) as record_count 
from invoice_item 
join item on invoice_item.item_id = item.item_id 
join invoice on invoice_item.invoice_id = invoice.invoice_id 
where item.type = 'E' and invoice_item.type = 'Purchase' 
group by invoice.invoice_id, item.item_id 
having count(invoice_item.invoice_item_id) > 1;

-- 12
select invoice.invoice_uuid, person.first_name, person.last_name as fraudulent_employee, company.name as company_name 
from invoice, person, company 
where invoice.salesperson_id = person.person_id 
and invoice.customer_id = company.company_id 
and invoice.salesperson_id = company.primary_contact_id;