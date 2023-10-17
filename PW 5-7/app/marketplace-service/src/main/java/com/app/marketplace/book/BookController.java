package com.app.marketplace.book;

import com.app.marketplace.auth.RequiresRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/marketplace/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    @GetMapping
    @RequiresRole({"USER", "SELLER", "ADMINISTRATOR"})
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @RequiresRole({"USER", "SELLER", "ADMINISTRATOR"})
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @PutMapping("{id}")
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ofNullable(bookService.updateBook(id, book).orElse(null));
    }

    @DeleteMapping("/{id}")
    @RequiresRole({"SELLER", "ADMINISTRATOR"})
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.getBookById(id).isEmpty())
            return ResponseEntity.internalServerError().body("There is no such book");

        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}