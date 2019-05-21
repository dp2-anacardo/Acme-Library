package services;

import domain.Actor;
import domain.Reader;
import domain.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.TransactionRepository;

import java.util.Collection;

@Service
@Transactional
public class TransactionService {

    //Managed repository
    private TransactionRepository transactionRepository;
    //Services
    private ActorService actorService;
    private ReaderService readerService;

    public Transaction create(){
        Assert.isTrue(this.actorService.getActorLogged() instanceof Reader);
        Transaction transaction = new Transaction();
        return transaction;
    }

    public Transaction findOne(int id){
        return this.transactionRepository.findOne(id);
    }

    public Collection<Transaction> findAll(){
        return this.transactionRepository.findAll();
    }
}
