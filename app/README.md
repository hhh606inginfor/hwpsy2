# HW PSY 2

Come secondo homework si è proceduto con una esercitazione in ambiente android sul multitasking.

Ho scelto di realizzare un'app che simuli un semplice gneratore di codici OTP. 

La generazione avviene in un thread separato dal thread principale ed avviene ogni 10 secondi 
indefinitivamente. I valori generati e il timestamp relativo al momento della genrazione vengono 
salvati su un database SQlite che viene creato al primo avvio della app. La generazione dei condici 
parte alla avvio della app. e termina al suo spegnimento. 

La app è divisa in 3 fragment denominati rispettivamente home, OTP  e Log.

- la Home  è semplicemente una schermata di benvenuto dove viene mostrato il logo di Uninettuno 
e il titolo dell'esercitazione e la matricola 
- nella pagina OTP  è possibile trovare un pulsate visualizza con il quale è possibile vedere l'utimo 
OTP generato, leggendolo dal database SQLite.
- nella pagina dedicata ai log vine motrato un elenco di tutti gli opt generati ordinati per data desc
e la data e ora in cui sono stati generati. Tramite un meccanismo di intent viene notificato il refresh della lista 
contestualmente alla generazione di un nuovo token. Nella pagina esiste inoltre un pulsante di pulizia del DB 
il quale dopo aver svuotato il db comunica tramite intent il refresh dei dati nella pagina.