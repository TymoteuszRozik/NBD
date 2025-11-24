package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryManagementService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final LibraryItemRepository itemRepository;

    public User createUser(User user) {
        user.setPersonType("USER");
        return (User) personRepository.save(user);
    }

    public Librarian createLibrarian(Librarian librarian) {
        librarian.setPersonType("LIBRARIAN");
        return (Librarian) personRepository.save(librarian);
    }

    public List<User> getAllUsers() {
        return personRepository.findAllUsers();
    }

    public Book createBook(Book book) {
        book.setItemType("BOOK");
        return bookRepository.save(book);
    }

    public Magazine createMagazine(Magazine magazine) {
        magazine.setItemType("MAGAZINE");
        return itemRepository.save(magazine);
    }

    public List<Book> findBooksByAuthor(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    public List<LibraryItem> searchItemsByTitle(String title) {
        return itemRepository.findByTitleContaining(title);
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public void addBookToAuthor(String authorId, String bookId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        author.getBookIds().add(bookId);
        authorRepository.save(author);
    }

    public List<Author> getAuthorsWithBooks() {
        return authorRepository.findAuthorsWithBooks();
    }
}