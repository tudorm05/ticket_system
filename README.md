*MIHAILA Tudor 325CA*
# ----- DesignPatterns -----
In tema am folosit 6 design pattern-uri: factory method, builder,
observer, visitor, command si sigleton.

Factory l-am folosit pentru a construi userii si ticketele, deoarece 
exista mai multe tipuri.

Builder pentru a construi TicketAction, pentru ca are unele campuri
care nu sunt obligatorii.

Observer l-am folosit pentru notificari pentru a notifica automat developerii.
Initial nu am implementat prin acest design pattern trimiterea notificarilor,
dar la un momentat dat am intrat pe ocw la observer si scria ca este "folosit 
atunci când ai un obiect (denumit Subject) care trebuie să notifice automat 
alte obiecte" si am facut conexiunea :).

Visitor l-am folosit pentru cele 4 tipuri de rapoarte, pentru ca formulele
de calcul erau diferite pentru fiecare tip de ticket.Initial am incercat
sa folosesc strategy, dar mi-am dat seama ca nu ma ajuta.

Command am folosit pentru comenzi.Astfel am incapsulat fiecare comanda
intr-o clasa proprie.M-a ajutat mult la debug.

Singleton l-am folosit la Database, deoarece permite existenta unei
singure baze de date.
# ----- Clase -----
### ----- Clase de Input -----
TicketInput, CommandInput si UserInput le-am folosit pentru a citi
inputul din inputPath.Ele au toate campurile posibile ce pot aparea
intr-un ticket, intr-un user sau o comanda.Le-am pus si getters,
pentru ca altfel nu se poate face citirea
### ----- Database -----
Database este cam cea mai importanta clasa din program, deoarece
retine userii, ticketele, milestoneurile si data de start(timestampul)
primei comenzi.

Are doua functii importante ce sunt apelate la fiecare
comanda.Aceste doua functii au rolul de a updata tot sistemul,
ticketele din milestone-uri deblocate isi maresc prioritatea la 3 zile.
De asemenea, a doua comanda de update anunta utilizatorii unui milestone
cand acesta este deblocat.Restul comenzilor sunt pentru diferite cautari
cum ar fi un ticket dupa id, un user dupa nume, etc.
### ----- User -----
Clasa User am facut-o pentru a fi mostenita de clasele Developer,
Reporter si Manager.Contine campurile comune ale celor 3 subclase

Developer este singura care implementeaza interfata Observer. 
Am facut asta pentru ca doar developerii trebuie sa primeasca notificari.
De asemenea, clasa Developer are metodele checkExpertise si checkSeniority,
pe care le apelez in comanda AssignTicket pentru a vedea daca poate prelua
un anumit ticket sau nu.

UserFactory face parte din Factory Method si l-am folosit pentru a crea userul 
bun in functie de campul role.
### ----- Ticket -----
Clasa Ticket este abstracta si e mostenita de Bug, FeatureRequest si UiFeedback.
Astea 3 sunt obiectele vizitate de design patternul visitor.

BugCreator, UiFeedbackCreator si FeatureRequestCreator sunt folosite pentru Factory;
### ----- TicketAction -----
Am facut aceasta clasa pentru a retine istoricul unui ticket.Am folosit builder
la aceasta clasa, deoarece unele campuri sunt obligatorii.De exemplu, milestoneName
apare doar cand actiuna asupra o reprezinta adaugarea intr-un milestone.
### ----- Milestone -----
Milestone mosteneste Subject, deoarece mentine lista de observeri (developerii).
Daca starea acestuia se schimba developerii sunt notificati.

Are mai multe metode pentru a returna ticketele closed, cele deschise, pentru a calcula
completionPercentage si pentru a calcula numarul de zile ramase pana la overdueBy si
dueDate.
### ----- Commands -----
Clasa abstracta Command mosteneste numele design patternului pe care il implementeaza.
Initial a fost interfata, dar am modificat-o in clasa abstracta, deoarece am creat metoda
createNode ce creeaza un ObjectNode si il populeaza cu partea comuna a tuturor comenzilor
care genereaza un output sau o eroare.

Command creator nu foloseste FactoryMethod, dar creeaza fiecare comanda incapsulata
in subclasele care mostenesc Command.
### ----- Report -----
Fiecare tip de report foloseste Visitor, deoarece pentru fiecare subclasa de ticket
exista cate o formula diferita pentru a calcula procentajul.