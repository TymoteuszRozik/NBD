-- Insert Librarians and Users (using separate tables as per your Hibernate schema)
INSERT INTO librarian (id, first_name, last_name, email, employee_id)
VALUES
    (1, 'Anna', 'Kowalska', 'anna.kowalska@biblioteka.pl', 'EMP001');

INSERT INTO library_user (id, first_name, last_name, email, reader_card_number)
VALUES
    (2, 'Jan', 'Nowak', 'jan.nowak@email.com', 'READER001'),
    (3, 'Maria', 'Wisniewska', 'maria.wisniewska@email.com', 'READER002');

-- Insert Authors
INSERT INTO author (id, name, nationality)
VALUES
    (1, 'Adam Mickiewicz', 'Polish'),
    (2, 'Stephen King', 'American'),
    (3, 'Agatha Christie', 'British');

-- Insert Library Items
INSERT INTO libraryitem (id, available, publication_year, title, version)
VALUES
    (1, true, 1834, 'Pan Tadeusz', 0),
    (2, true, 1986, 'To', 0),
    (3, true, 2024, 'National Geographic', 0);

-- Insert Book-specific data
INSERT INTO book (id, isbn, publisher)
VALUES
    (1, '978-83-08-06008-7', 'Wydawnictwo Literackie'),
    (2, '978-83-08-05376-8', 'Albatros');

-- Insert Magazine-specific data
INSERT INTO magazine (id, issue_number)
VALUES
    (3, 245);

-- Link books to authors
INSERT INTO book_authors (book_id, author_id)
VALUES
    (1, 1),
    (2, 2);