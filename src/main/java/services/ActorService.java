
package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ActorService {

    //Managed Repositories
    @Autowired
    private ActorRepository actorRepository;

    //Supporting services
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private MessageService messageService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private FinderService finderService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private BookService bookService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private RefereeService refereeService;
    @Autowired
    private EventService eventService;
    @Autowired
    private OrganizerService organizerService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private SponsorService sponsorService;
    @Autowired
    private SponsorshipService sponsorshipService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SocialProfileService socialProfileService;
    @Autowired
    private AdministratorService administratorService;

    public Collection<Actor> findAll() {
        Collection<Actor> result;

        result = this.actorRepository.findAll();

        return result;
    }

    public Actor findOne(final int actorId) {
        Assert.isTrue(actorId != 0);

        Actor result;

        result = this.actorRepository.findOne(actorId);

        return result;
    }

    public void delete(final Actor actor) {
        Assert.notNull(actor);
        Assert.isTrue(actor.getId() != 0);
        Assert.isTrue(this.actorRepository.exists(actor.getId()));

        this.actorRepository.delete(actor);
    }

    public Actor findByUserAccount(final UserAccount userAccount) {
        Assert.notNull(userAccount);

        Actor result;

        result = this.actorRepository.findByUserAccountId(userAccount.getId());

        return result;
    }

    public Actor getActorLogged() {
        UserAccount userAccount;
        Actor actor;

        userAccount = LoginService.getPrincipal();
        Assert.notNull(userAccount);

        actor = this.findByUserAccount(userAccount);
        Assert.notNull(actor);

        return actor;
    }

    public Actor save(final Actor actor) {
        Assert.notNull(actor);

        Actor result;

        result = this.actorRepository.save(actor);

        return result;
    }

    public Actor findByUsername(final String username) {
        Assert.notNull(username);

        Actor result;
        result = this.actorRepository.findByUsername(username);
        return result;
    }

    public Boolean existUsername(final String username) {
        Boolean res = false;
        final List<String> lista = new ArrayList<String>();
        for (final Actor a : this.actorRepository.findAll())
            lista.add(a.getUserAccount().getUsername());
        if (!(lista.contains(username)))
            res = true;
        return res;
    }

    public Boolean existIdSocialProfile(final Integer id) {
        Boolean res = false;
        final List<Integer> lista = new ArrayList<Integer>();
        for (final SocialProfile s : this.socialProfileService.findAll())
            lista.add(s.getId());
        if (lista.contains(id))
            res = true;
        return res;
    }

    public void deleteInformation() {

        UserAccount userAccount;
        userAccount = LoginService.getPrincipal();
        final Actor user = this.findByUserAccount(userAccount);

        //Borrado de los socialProfiles de los actores
        if (!(user.getSocialProfiles().isEmpty())) {
            final List<SocialProfile> a = new ArrayList<>();
            final Collection<SocialProfile> ad = user.getSocialProfiles();
            a.addAll(ad);
            for (final SocialProfile i : a)
                this.socialProfileService.delete(i);
        }

        //Borrado de los mensajes que recibes
        final Collection<Message> msgs = this.messageService.findAllReceivedByActor(user.getId());
        if (msgs.size() > 0) {
            for (final Message m : msgs) {
                for (final MessageBox b : user.getBoxes()) {
                    if (b.getMessages().contains(m)) {
                        b.deleteMessage(m);
                        m.getMessageBoxes().remove(b);
                    }
                }
            }
        }

        //Borrado de los mensajes que envias
        final Collection<Message> sent = this.messageService.findAllSentByActor(user.getId());
        if (sent.size() > 0) {
            for (final Message s : sent) {
                for (final MessageBox b1 : user.getBoxes()) {
                    if (b1.getMessages().contains(s)) {
                        b1.deleteMessage(s);
                        s.getMessageBoxes().remove(b1);
                    }
                }
                s.setSender(null);
            }
        }

        //Borrado cuando el usuario logueado es administrador
        if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN")) {
            final Administrator a = this.administratorService.findOne(user.getId());
            this.administratorService.delete(a);
        }

        //Borrado cuando el usuario logueado es referee
        if (userAccount.getAuthorities().iterator().next().getAuthority().equals("REFEREE")) {
            final Referee r = this.refereeService.findOne(user.getId());
            Collection<Complaint> collectionComplaint = this.complaintService.getComplaintsByReferee(r.getId());
            if (collectionComplaint.size() > 0) {
                for (final Complaint c : collectionComplaint) {
                    Collection<Report> reports = this.reportService.getReportsByComplaint(c.getId());
                    if (reports.size() > 0) {
                        for (final Report r1 : reports) {
                            for (final Comment comments : r1.getComments()) {
                                this.commentService.delete(comments.getId());
                            }
                            this.reportService.delete(r1.getId());
                        }
                    }
                    c.setReferee(null);
                }
            }
            this.refereeService.delete(r);
        }

        //Borrado cuando el usuario logueado es sponsor
        if (userAccount.getAuthorities().iterator().next().getAuthority().equals("SPONSOR")) {
            final Sponsor sponsor = this.sponsorService.findOne(user.getId());
            final Collection<Sponsorship> sponsorships = this.sponsorshipService.findBySponsor(sponsor.getId());
            if (sponsorships.size() > 0) {
                for (final Sponsorship s : sponsorships) {
                    this.sponsorshipService.delete(s);
                }
            }
            this.sponsorService.delete(sponsor);
        }

        //Borrado cuando el usuario logueado es organizer
        if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ORGANIZER")) {
            final Organizer organizer = this.organizerService.findOne(user.getId());
            final List<Event> eventos = this.eventService.findAllByOrganizer(organizer.getId());
            if (eventos.size() > 0) {
                for (final Event e : eventos) {
                    if (this.sponsorshipService.findAllByEvent(e.getId()).size() > 0) {
                        for (Sponsorship s : this.sponsorshipService.findAllByEvent(e.getId())) {
                            this.sponsorshipService.delete(s);
                        }
                    }
                    if (e.getRegisters().size() > 0) {
                        for (final Register r : e.getRegisters()) {
                            this.registerService.delete(r.getId());
                        }
                    }
                    this.eventService.deleteForced(e);
                }
            }
            this.organizerService.delete(organizer);
        }
        //Borrado cuando el usuario logueado es reader
        if (userAccount.getAuthorities().iterator().next().getAuthority().equals("READER")) {
            final Reader reader = this.readerService.findOne(user.getId());

            // Complaints del reader, sus reportes y comentarios.
            Collection<Complaint> complaints = this.complaintService.getComplaintsByReader(reader.getId());
            for (final Complaint c : complaints) {
                Collection<Report> reports = this.reportService.getReportsByComplaint(c.getId());
                for (final Report r1 : reports) {
                    for (final Comment comments : r1.getComments())
                        this.commentService.delete(comments.getId());
                    this.reportService.delete(r1.getId());
                }
                this.transactionService.getTransactionByComplaint(c.getId()).getComplaints().remove(c);
                this.complaintService.delete(c.getId());
            }

            Collection<Register> registers = this.registerService.getRegistersByReader(reader.getId());
            if (registers.size() > 0) {
                for (final Register r : registers) {
                    Event e = this.eventService.findByRegister(r.getId());
                    e.getRegisters().remove(r);
                    this.registerService.delete(r.getId());
                }
            }

            // Borra las transacciones de ventas del reader, si todavía no ha sido finalizadas, se borra de los finders.
            // Se borran las complaints, reports y comentarios de esa transaction.
            Collection<Transaction> salesByReader = this.transactionService.getSalesByReader();
            for (Transaction t : salesByReader) {
                if (!t.getIsFinished()) {
                    Collection<Finder> finders = this.finderService.findAllByTransaction(t.getId());
                    for (Finder f : finders)
                        f.getTransactions().remove(t);
                } else {
                    Collection<Complaint> transactionComplaints = t.getComplaints();
                    for (final Complaint ct : transactionComplaints) {
                        Collection<Report> complaintReports = this.reportService.getReportsByComplaint(ct.getId());
                        for (final Report r : complaintReports) {
                            for (final Comment comments : r.getComments())
                                this.commentService.delete(comments.getId());
                            this.reportService.delete(r.getId());
                        }
                        this.transactionService.getTransactionByComplaint(ct.getId()).getComplaints().remove(ct);
                        this.complaintService.delete(ct.getId());
                    }
                }
                this.transactionService.delete3(t);
            }

//            Borra las transacciones de compra del reader, pero no sé si es necesario,
//            ya que al estar en finalizada tampoco debería pasar nada.
//            Collection<Transaction> buysByReader = this.transactionService.getBuysByReader();
//            for (Transaction tb : buysByReader)
//                this.transactionService.delete3(tb);

            // Borra las transacciones de intercambio del reader, se borran todas las ofertas.
            Collection<Transaction> exchangesByReader = this.transactionService.getExchangesByReader();
            for (Transaction te : exchangesByReader) {
                for (Offer oe : te.getOffers()) {
                    te.getOffers().remove(oe);
                    this.offerService.delete(oe);
                }
                if (!te.getIsFinished()) {
                    Collection<Finder> finders = this.finderService.findAllByTransaction(te.getId());
                    for (Finder f : finders)
                        f.getTransactions().remove(te);
                } else {
                    Collection<Complaint> transactionComplaints = te.getComplaints();
                    for (final Complaint ct : transactionComplaints) {
                        Collection<Report> complaintReports = this.reportService.getReportsByComplaint(ct.getId());
                        for (final Report r : complaintReports) {
                            for (final Comment comments : r.getComments())
                                this.commentService.delete(comments.getId());
                            this.reportService.delete(r.getId());
                        }
                        this.transactionService.getTransactionByComplaint(ct.getId()).getComplaints().remove(ct);
                        this.complaintService.delete(ct.getId());
                    }
                }
                this.transactionService.delete3(te);
            }

            // Borra las ofertas que ha hecho el reader.
            Collection<Offer> offersByReader = this.offerService.getAllByReader(reader.getId());
            for (Offer or : offersByReader) {
                or.getTransaction().getOffers().remove(or);
                this.offerService.delete(or);
            }

            // Borra los libros una vez no están referenciados en ningún sitio.
            Collection<Book> readerBooks = this.bookService.getBooksByReader(reader.getId());
            for (Book b : readerBooks)
                this.bookService.deleteForced(b);

            // Borra el reader
            this.readerService.delete(reader);

            // Una vez borrado el reader se borra el finder.
            Finder finder = this.finderService.findOne(reader.getFinder().getId());
            finder.setTransactions(new ArrayList<Transaction>());
            this.finderService.delete(finder);

        }
    }
}
