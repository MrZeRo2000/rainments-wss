ALTER TABLE products ADD COLUMN product_counter_precision INT;

UPDATE products SET 
  product_unit_name = NULL
WHERE product_unit_name = '';  

UPDATE products SET 
  product_counter_precision = 2
WHERE product_unit_name IS NOT NULL;  

ALTER TABLE payment_objects ADD COLUMN payment_object_period TEXT;
ALTER TABLE payment_objects ADD COLUMN payment_object_term TEXT;
ALTER TABLE payment_objects ADD COLUMN payment_object_pay_delay INT;

COMMIT;
