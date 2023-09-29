package com.app.marketplace.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> updateBook(Long id, Book updatedBook) {
        Optional<Book> bookOptional = getBookById(id);
        if (bookOptional.isEmpty())
            return Optional.empty();

        Book dbBook = bookOptional.get();

        if (updatedBook.getAuthor() != null && !updatedBook.getAuthor().isBlank())
            dbBook.setAuthor(updatedBook.getAuthor());

        if (updatedBook.getPrice() > 0)
            dbBook.setPrice(updatedBook.getPrice());

        if (updatedBook.getTitle() != null && !updatedBook.getTitle().isBlank())
            dbBook.setTitle(updatedBook.getTitle());

        if (updatedBook.getProductType() != null && !updatedBook.getProductType().isBlank())
            dbBook.setProductType(updatedBook.getProductType());

        if (updatedBook.getSellerNumber() != null && !updatedBook.getSellerNumber().isBlank())
            dbBook.setSellerNumber(updatedBook.getSellerNumber());

        return Optional.of(saveBook(dbBook));
    }
}
