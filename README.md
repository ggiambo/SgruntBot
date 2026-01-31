# SgruntBot

Tentativo di riscrittura di Sgrunty usando un linguaggio moderno, giovane e dinamico, proprio come te!

## Come lo faccio partire?

Frena campione! Prima devi settare qualche env-variable:
- CHAT_ID: Lo dice il nome, è l'ID della chat nella quale Sgrunty elagirà le sue perle di saggezza
- TELEGRAM_TOKEN: Questo è il token del tuo bot, L'hai creato usando Botfather, ricordi?
- IMGUR_CLIENT_ID: Credo non sia necessario. Settalo come vuoi probabilmente funziona lo stesso
- GEMINI_API_KEY: Lo dice il nome. Usato per l'Oroscopo. Ma fa schifo, mi sa che lo rimuovo.
- GH_TOKEN: Il tuo token per GitHub. Basta che abbia l'accesso Readonly.

E devi pure avere mariadb che gira locale, con un database "sgrunt", un utente "sgrunt" con la password "sgrunt". Facile, no?

```mariadb
create user 'sgrunt'@localhost identified by 'sgrunt';
grant all privileges on sgrunt.* to 'sgrunt'@'%' identified by 'sgrunt';
flush privileges;
```

Pronti? Bene, ora fai partire Sgrunty!

```shell
./mvnw spring-boot:run
```

Se vuoi usare un proxy HTTP, usa qualcosa del tipo `-proxy http://localhost:8080`\
Per una resa ottimale, assicurati di aver installato `fortunes-it` e l'ultimissima versione di `yt-dlp`.

Vedi anche il file `src/main/resources/schema.sql`

## API

Non sono il massimo, ma forse possono piacerti le [REST API](http://localhost:8081/sgrunty/swagger-ui/index.html) (o [qui](https://giambo.ch/sgrunty/swagger-ui/index.html))

## Docker! Container! Blah&Banf!

1. Builda sgrunty
```shell
./mvnw clean install
```
1. Lancia sgrunty in tutta la sua magnificenza
```shell
cd docker
docker compose up
```
   
Il database sarà accessibile sulla porta 3307