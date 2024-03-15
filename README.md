# SgruntBot [![Java CI with Gradle](https://github.com/ggiambo/SgruntBot/actions/workflows/gradle.yml/badge.svg)](https://github.com/ggiambo/SgruntBot/actions/workflows/gradle.yml)

Tentativo di riscrittura di Sgrunty usando un linguaggio moderno, giovane e dinamico, proprio come te!

## Come lo faccio partire?

Prima di tutto devi avere un file `token.txt` contenente il token per il bot, altrimenti ciccia.\
Il comando magico per far partire il tutto è:

```shell
./gradlew run
```

Se vuoi usare un proxy HTTP, usa qualcosa del tipo `-proxy http://localhost:8080`\
Per una resa ottimale, assicurati di aver installato `fortunes-it` e l'ultimissima versione di `youtube-dl`.

Ah, ci sarebbe anche il token per imgur, un file chiamato `imgurClientId.txt`.

## Non funzionahhh!

LOL, ho dimenticato di dire che devi avere un database. MariaDB, per la precisione.\
E un utente "sgrunt" con password "sgrunt" con accesso a un database che si chiama ... "sgrunt"!

```mariadb
create user 'sgrunt'@localhost identified by 'sgrunt';
grant all privileges on sgrunt.* to 'sgrunt'@localhost identified by 'sgrunt';
flush privileges;
```

## API

Non sono il massimo, ma forse possono piacerti le [REST API](http://localhost:8081/sgrunty/swagger-ui/index.html)
