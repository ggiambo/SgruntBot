# SgruntBot
Tentativo di riscrittura di Sgrunty usando un linguaggio moderno, giovane e dinamico, proprio come te!
## Come lo faccio partire?
Prima di tutto devi avere maven. Niente gradle, non siamo amici io e gradle ☹️ \
Poi devi avere un file `token.txt` contenente il token per il bot, altrimenti ciccia.\
Il comando magico per far partire il tutto è:
```
mvn exec:java
```

Se vuoi usare un proxy HTTP, usa qualcosa del tipo `-proxy http://localhost:8080`\
Per una resa ottimale, assicurati di aver installato `fortune` e l'ultimissima versione di `youtube-dl`.
