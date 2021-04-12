Si sviluppi in Java il gioco BubbleBlast (che potete scaricare e provare dai vari play store) utilizzando come interfaccia la console.

Il gioco viene eseguito su una griglia di 5 * 6 dove possono essere presenti più bolle in 3 stati :

 i) in procinto di esplodere, 

2) gonfia a metà, 

3) sgonfia.

L’utente può selezionare una bolla ed ad ogni selezione la gonfia facendola passare allo stato successivo es da sgonfia a metà e da metà a in procinto di esplodere. Se si seleziona una bolla in procinto di esplodere questa esploderà e i) scompare dalla griglia, ii) propaga l’esplosione nelle direzioni verticali e orizzontali

Quando una propagazione raggiunge una bolla questa si comporta come se fosse stata selezionata e si gonfierà passando allo stato successivo. Se la bolla è già nello stato in procinto di esplodere esploderà propagando l’esplosione a sua volta.

Il gioco termina quando non ci sono più bolle sulla griglia. Il giocatore vince se riesce a far scoppiare tutte le bolle in un numero di mosse che il gioco stesso deve stimare.

Il gioco deve fornire delle griglie random con bolle nei vari stati.
Il gioco deve trovare il numero minimo di mosse da effettuare, prima di proporre al giocatore la griglia.



Scrivere il codice per realizzare il gioco descritto:

Usando Streams, Lambda e Optional per gestire eventuali valori nulli
Alla fine della partita salvare un file testuale che elenchi tutte le mosse effettuate dal giocatore ed il loro effetto
