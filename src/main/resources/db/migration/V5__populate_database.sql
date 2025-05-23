INSERT INTO categories (name)
VALUES ('Fruits'),
       ('Vegetables'),
       ('Dairy'),
       ('Bakery'),
       ('Beverages');

INSERT INTO products (name, price, description, category_id)
VALUES ('Bananas', 1.99, 'Fresh organic bananas, sold by the bunch.', 1),
       ('Red Apples', 2.49, 'Crisp and juicy red apples, perfect for snacking.', 1),
       ('Broccoli', 1.79, 'Fresh green broccoli, ideal for steaming or stir-fry.', 2),
       ('Carrots', 0.99, 'Sweet and crunchy carrots, sold in 1kg bags.', 2),
       ('Whole Milk', 3.49, '1-liter whole milk from local dairy farms.', 3),
       ('Cheddar Cheese', 4.99, 'Aged cheddar cheese block, 250g.', 3),
       ('Sourdough Bread', 3.29, 'Handmade sourdough loaf with crusty outside and soft inside.', 4),
       ('Croissants', 2.99, 'Buttery croissants, pack of 4.', 4),
       ('Orange Juice', 2.79, '100% pure orange juice with no added sugar, 1L.', 5),
       ('Bottled Water', 0.99, '500ml spring water, convenient and refreshing.', 5);
