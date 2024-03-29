package com.mbooks.microservicebooks.dao;

import com.mbooks.microservicebooks.model.Book;
import com.mbooks.microservicebooks.model.Borrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private BorrowingDao borrowingDao;

    public void setBookAvailability(Book book){
        boolean availableOrNot = true;
        String bookRef = book.getRef();
        int notReturnedYet = 0;
        int booksAvailable = book.getNbr();
        //list all borrowings for this book
        List<Borrowing> borrowingsByBook = borrowingDao.findBorrowingByBook_Id(book.getId());
        //if there is at least one borrowing with this book ref
        if (borrowingsByBook.size() > 0) {
            //notReturnedYet occurences where the book is not returned yet
            for (Borrowing borrowing : borrowingsByBook) {
                if (borrowing.getReturned() == null) {
                    notReturnedYet++;
                }
            }
            //available books minus those borrowed that have not been returned
            booksAvailable -= notReturnedYet;
            //if all borrowed books with this ref have not been returned, then this book is not available
            if (notReturnedYet >= borrowingsByBook.size()) {
                availableOrNot=false;
            }
        } else {
            availableOrNot=true;
        }
        book.setAvailable(availableOrNot);
        book.setAvailableBooksNbr(booksAvailable);
    }
}
