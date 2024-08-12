-- Insert data into breed
INSERT INTO breed (name, description) VALUES
('Labrador', 'Friendly and outgoing dog breed'),
('Persian', 'Long-haired and docile cat breed'),
('Parakeet', 'Small and social bird breed'),
('Goldfish', 'Popular and easy-to-care-for fish breed'),
('Gecko', 'Small lizard often kept as a pet');

-- Insert data into pet_type
INSERT INTO pet_type (type, breed_id) VALUES
('DOG', 1),
('CAT', 2),
('BIRD', 3),
('FISH', 4),
('REPTILE', 5);

-- Insert data into shelter
INSERT INTO shelter (is_active, phone, size, vat, address1, address2, email, name, postal_code) VALUES
(true, 123456789, 100, 123456, '123 Shelter St.', '', 'shelter1@example.com', 'Happy Paws', '1000-000'),
(true, 987654321, 50, 654321, '456 Shelter Ave.', 'Apt 1', 'shelter2@example.com', 'Safe Haven', '2000-000'),
(true, 123123123, 75, 111111, '789 Shelter Rd.', '', 'shelter3@example.com', 'Pet Paradise', '3000-000'),
(true, 321321321, 150, 222222, '321 Shelter Blvd.', '', 'shelter4@example.com', 'Animal Care', '4000-000'),
(true, 456456456, 80, 333333, '654 Shelter Ln.', '', 'shelter5@example.com', 'Critter Haven', '5000-000');

-- Insert data into person
INSERT INTO person (first_name, last_name, cell_phone, nif, postal_code, address1, address2, email, password, role) VALUES
('John', 'Doe', 912345678, 123456789, 1000, '123 Street', '', 'john.doe@example.com', 'password123', 'USER'),
('Jane', 'Smith', 987654321, 987654321, 2000, '456 Avenue', '', 'jane.smith@example.com', 'password123', 'USER'),
('Alice', 'Johnson', 923456789, 456789123, 3000, '789 Boulevard', '', 'alice.johnson@example.com', 'password123', 'ADMIN'),
('Bob', 'Williams', 876543219, 321456987, 4000, '321 Road', '', 'bob.williams@example.com', 'password123', 'MANAGER'),
('Charlie', 'Brown', 954321789, 654789321, 5000, '654 Lane', '', 'charlie.brown@example.com', 'password123', 'USER');

-- Insert data into pet
INSERT INTO pet (name, age, is_adopted, is_vaccinated, weight, pet_type_id, shelter_id, color, observations, size) VALUES
('Buddy', 3, false, true, 25.5, 1, 1, 'Golden', 'Very friendly dog', 'MEDIUM'),
('Mittens', 2, true, true, 5.5, 2, 2, 'White', 'Loves to sleep all day', 'SMALL'),
('Tweety', 1, false, false, 0.1, 3, 3, 'Yellow', 'Chirpy little bird', 'SMALL'),
('Goldie', 1, false, true, 0.2, 4, 4, 'Orange', 'Swims around happily', 'SMALL'),
('Gecko', 4, true, true, 0.5, 5, 5, 'Green', 'Very calm lizard', 'SMALL');

-- Insert data into adoption_request
INSERT INTO adoption_request (adopter_id, pet_id, shelter_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5);

-- Insert data into donation
INSERT INTO donation (total, date, person_id, shelter_id) VALUES
(100.0, '2023-01-01 12:00:00', 1, 1),
(150.0, '2023-02-01 13:00:00', 2, 2),
(200.0, '2023-03-01 14:00:00', 3, 3),
(250.0, '2023-04-01 15:00:00', 4, 4),
(300.0, '2023-05-01 16:00:00', 5, 5);

-- Insert data into pet_record
INSERT INTO pet_record (date, pet_id, observation, pet_records_status) VALUES
('2023-01-01 12:00:00', 1, 'Vaccination done', 'HEALTHY'),
('2023-02-01 13:00:00', 2, 'Adopted by a family', 'ADOPTED'),
('2023-03-01 14:00:00', 3, 'Showing good behavior', 'HEALTHY'),
('2023-04-01 15:00:00', 4, 'Vaccination done', 'HEALTHY'),
('2023-05-01 16:00:00', 5, 'Adopted by an individual', 'ADOPTED');

-- Insert data into request_details
INSERT INTO request_details (adoption_request_id, date, person_id, observation, state) VALUES
(1, '2023-01-01 12:00:00', 1, 'Request sent for adoption', 'SENT'),
(2, '2023-02-01 13:00:00', 2, 'Verification in progress', 'VERIFYING_INFORMATION'),
(3, '2023-03-01 14:00:00', 3, 'Missing some documents', 'MISSING_INFORMATION'),
(4, '2023-04-01 15:00:00', 4, 'Request accepted', 'ACCEPTED'),
(5, '2023-05-01 16:00:00', 5, 'Request refused', 'REFUSED');

-- Insert data into shelter_person_roles
INSERT INTO shelter_person_roles (person_id, shelter_id, role) VALUES
(1, 1, 'USER'),
(2, 2, 'USER'),
(3, 3, 'ADMIN'),
(4, 4, 'MANAGER'),
(5, 5, 'USER');
