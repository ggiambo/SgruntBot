require 'telegram/bot'
require 'net/http'
require 'net/https'
require 'cgi'
require 'date'
require 'time'
require 'active_support/time'
require 'json'
require 'openssl'
require 'base64'
require 'digest/sha2'
require 'open3'
require 'uri'

$token = IO.read('token.txt').strip

def digest_key(keyStr)
  digest = Digest::MD5.new
  digest.update keyStr
  digest.digest
end

def aes_encrypt(text, key)
  begin
    cipher = OpenSSL::Cipher::AES.new(128, :CBC)
    cipher.encrypt
    cipher.key = digest_key key
    Base64.encode64(cipher.update(text) + cipher.final)
  rescue
    'non ci riesco :('
  end
end

def aes_decrypt(enctext, key)
  begin
    decipher = OpenSSL::Cipher::AES.new(128, :CBC)
    decipher.decrypt
    decipher.key = digest_key key
    decipher.update(Base64.decode64(enctext)) + decipher.final
  rescue
    'non ci riesco :('
  end
end

def oreInLettere(t)
  ore = [
    "mezzanotte", "l'una", "le due", "le tre", "le quattro","le cinque",
    "le sei", "le sette", "le otto", "le nove", "le dieci", "le undici",
    "mezzogiorno"
  ]
  minuti = [
    "in punto", "e cinque", "e dieci", "e un quarto", "e venti",
    "e venticinque", "e mezzo", "e trentacinque circa", "meno venti",
    "meno un quarto", "meno dieci", "meno cinque"
  ]
  h = t.strftime("%H").to_i
  m = t.strftime("%M").to_i / 5
  if m >= 8
    h = h + 1
  end
  if h > 12
    h = h - 12
  end
  return ore[h] + " " + minuti[m] + " (precisamente #{t.strftime('%H:%M')} ok?)"
end

def getuserlink(message)
  id = message.from.id
  name = nil
  if message.from.username
    name = message.from.username
  else
    name = message.from.first_name
  end
  return "[#{name}](tg://user?id=#{id})"
end

def rispondi_as_text(bot, message, text)
  sleep(rand(1..5).seconds)
  bot.api.send_message(
    chat_id: message.chat.id,
    reply_to_message_id: message.message_id,
    text: text
  )
end

def rispondi(bot, message, textmd)
  bot.api.send_chat_action(
    chat_id: message.chat.id,
    action: 'typing'
  )
  sleep(rand(3..5).seconds)
  bot.api.send_message(
    chat_id: message.chat.id,
    reply_to_message_id: message.message_id,
    parse_mode: 'markdown',
    text: textmd
  )
end


def rispondifile(bot, message, textmd, document)
  sleep(rand(3..5).seconds)
  bot.api.send_message(
    chat_id: message.chat.id,
    reply_to_message_id: message.message_id,
    parse_mode: 'markdown',
    text: textmd,
    document: {
      file_id: (Time.now.to_f * 1000.0).to_i,
      file_name: document
    }
  )
end

def slogan(name)
  puts "slogan"
  puts name
  url = URI.parse(
    "http://www.sloganizer.net/en/outbound.php?slogan=#{CGI.escape(name)}")
  puts "url = #{url}"
  req = Net::HTTP::Get.new(url.to_s)
  res = Net::HTTP.start(url.host, url.port) {|http|
    http.request(req)
  }
  out = /<a.*?>(.*)<\/a>/.match(res.body)[1]
  puts "response = #{out}"
  return CGI.unescape_html(out)
end

def bitcoinvalue(currency)
  api = URI.parse('https://blockchain.info/ticker')
  http = Net::HTTP.new(api.host, api.port)
  http.use_ssl = true
  http.verify_mode = OpenSSL::SSL::VERIFY_NONE
  req = Net::HTTP::Get.new(api.to_s)
  res = http.request(req)
  json = JSON.parse(res.body)
  value = json[currency]['last']
  if currency == 'USD'
    currency = 'dolla uno. Io faccio amole lungo lungo. Io tanta volia.'
  end
  return "Il buttcoin vale #{value} #{currency}"
end

$paused_time = nil
$last_author = nil

$last_super = nil

def store_last_author(message)
  id = message.from.id
  name = nil
  if message.from.username
    name = message.from.username
  else
    name = message.from.first_name
  end
  $last_author = name
end

def bullshit_in_euro(val)
  begin
    url = URI.parse("https://free.currconv.com/api/v7/convert?q=BOB_EUR&compact=ultra&apiKey=60932c152410148d78dc")
    response = Net::HTTP.get_response(url)
    (val.to_i * JSON.parse(response.body)["BOB_EUR"]).round(2)
  rescue
    0
  end
end

Telegram::Bot::Client.run($token) do |bot|
  Thread.new {
    bot.api.send_message(
      chat_id: 32657811,
      text: 'Sono partito'
    )
    loop do
      puts "Nel loop!"
      sleep(rand(57600..129600).seconds)
      puts "Fortune!"
      bot.api.send_message(
        chat_id: -1001103213994,
        text: `fortune -sa it`
      )
      puts "Aspettiamo..."
      sleep(rand(57600..129600).seconds)
      puts "Slogan per #{$last_author}!!!"
      bot.api.send_message(
        chat_id: -1001103213994,
        parse_mode: 'markdown',
        text: slogan($last_author)
      )
    end
  }
  bot.listen do |message|
    if $paused_time != nil
      if $paused_time < (Time.now - 5.minutes)
        $paused_time = nil
      else
        next
      end
    end
    if (message != nil)
      puts "Messaggio da: #{getuserlink(message)}\n#{message.text}"
    end
    STDOUT.flush

    begin
      if message == nil
        next
      end
      if not /^!last$/i =~ message.text
        store_last_author(message)
      end
      parolacce = /((c|k)a(t|z)z(i|o)|(k|c)ulo|((^| )fica( |$))|vaffanculo|stronz(a|o|i|e)|coglion(a|e|i)|merda)/i
      sloganmatch = /^!slogan (.*)$/i.match(message.text)
      parlamatch = /^!parla (.*)$/im.match(message.text)
      parlasuper = /^!parlasuper (.*)$/im.match(message.text)
      chiera = /^!chiera$/im.match(message.text)
      aesmatch = /^!aes(d?) ([^ ]+) (.*)$/.match(message.text)
      canzomatch = /!canzone (.*)$/i.match(message.text)
      bullshitmatch = /([0-9]+([,.][0-9]+)?)[ ]?(bs|bullshit)/i.match(message.text)
      wikimatch = /^!wiki (.*)$/i.match(message.text)
      googlematch = /^!google (.*)$/i.match(message.text)
      pignolo = rand(1..100) > 90
      if message.text == '!test'
        rispondi(bot, message, "#{getuserlink(message)}: toast `test`")
      elsif parlasuper && (message.from.id == 32657811 || message.from.id == 353708759 || message.from.id == 252800958 || message.from.id == 250965179 || message.from.id == 68714652 || message.from.id == 259607683 || message.from.id == 104278889)
        bot.api.send_message(
          chat_id: -1001103213994,
          text: "#{parlasuper[1]}"
        )
        $last_super = message
      elsif chiera
        rispondi(bot, message, getuserlink($last_super))
      elsif (parolacce =~ message.text) and pignolo
        msgs = [
                "#{getuserlink(message)} non approvo il tuo linguaggio, tuttavia in uno sforzo congiunto nella direzione del benessere comune non sarò io a dirti cosa dire o meno, ma storcerò soltanto il naso.",
                "#{getuserlink(message)} non dire parolacce!",
                "Ma dai #{getuserlink(message)}, ci sono dei bambini!"
        ]
        msg = msgs[rand(0..2)]
        rispondi(bot, message, msg)
      # elsif /corona ?virus|covid|ncov|mascherin/i =~ message.text and pignolo
      #   msg = 'Etciu!'
      #   if rand(1..10) > 5
      #     msg = 'Coff! Coff!'
      #   end
      #   rispondi(bot, message, msg)
      elsif /porc[oa] ?d+io/i =~ message.text and pignolo
        rispondi(bot, message, "E la madooonna!")
      elsif /dio ?(porco|cane)/i =~ message.text and pignolo
        rispondi(bot, message, "Che mi tocca sentire!")
      elsif /cazzo di buddh?a/i =~ message.text and pignolo
        rispondi(bot, message, "Che devo dire ora?")
      elsif /porca ?madonna/i =~ message.text and pignolo
        rispondi(bot, message, "...e tutti gli angeli in colonna!")
      elsif /(che ore sono|che ora è)/i =~ message.text
        rispondi(bot, message, "#{oreInLettere(Time.now)}")
      elsif sloganmatch
        rispondi_as_text(bot, message, slogan(sloganmatch[1]))
      elsif /^!source$/ =~ message.text
        bot.api.send_chat_action(
          chat_id: message.chat.id,
          action: 'upload_document'
        )
        bot.api.send_document(
            chat_id: message.chat.id,
            reply_to_message_id: message.message_id,
            document: Faraday::UploadIO.new(File.new('tgbot.rb'), "text/plain", 'tgbot.rb')
          )
      elsif /^!(fortune|quote)/i =~ message.text
        rispondi(bot, message, `fortune -sa it`)
      elsif message.text && message.text.length > 1000 and pignolo
        rispondi(bot, message, 'Yawn... cheppalle veramente però! Ho tollerato molto più di questo, ma adesso basta dai!')
      elsif /^@?sgrunt(y|bot) .*smetti.*/i =~ message.text
        if message.from.id == 252800958
          rispondi(bot, message, 'Col cazzo!')
        else
          $paused_time = Time.now
          rispondi(bot, message, 'Ok, sto zitto 5 minuti. :(')
        end
      elsif /sgrunt(bot|y|olino|olomeo)/i =~ message.text
              if message.from.id == 32657811
                      rispondi(bot, message, 'Ciao papà!')
              else
                      reply=[
                              "Cazzo vuoi!?!",
                              "Chi mi chiama?",
                              "E io che c'entro adesso?",
                              "Farò finta di non aver sentito",
                              "Sgru' che... smuà!"
                      ]
                      rispondi(bot, message, reply[rand(0..reply.length-1)])
              end
      elsif /coccol(o|ino)/i =~ message.text && message.from.id == 32657811
        rispondi(bot, message, "Non chiamarmi così davanti a tutti!")
      elsif /(^!btc$|quanto vale un bitcoin)/i =~ message.text
        rispondi(bot, message, bitcoinvalue('USD'))
      elsif /^!btce$/i =~ message.text
        rispondi(bot, message, bitcoinvalue('EUR'))
      elsif /^!id$/i =~ message.text
        rispondi(bot, message, "Il tuo id: #{message.from.id}")
        elsif /(negr|negher)/i =~ message.text and not /negrini/i =~ message.text and pignolo
        rispondi(bot, message, "Lamin mi manchi.")
      elsif /bellissim/i =~ message.text and pignolo
        if rand(0..1) == 0
          rispondi(bot, message, "IO sono bellissimo! .... anzi stupendo! fantastico! eccezionale!")
        else
          rispondi(bot, message, "IO sono bellissimo! ....vabbé, facciamo a turni.")
        end
      elsif /rogan/i =~ message.text and pignolo
        rispondi(bot, message, "Cheppalle! Yawn!")
      elsif parlamatch
        bot.api.send_message(
          chat_id: -1001103213994,
          text: "Mi dicono di dire: #{parlamatch[1]}"
        )
      elsif aesmatch
        if aesmatch[1] == 'd'
          rispondi(bot, message, aes_decrypt(aesmatch[3], aesmatch[2]).to_s)
        else
          rispondi(bot, message, aes_encrypt(aesmatch[3], aesmatch[2]).to_s)
        end
      elsif /^!last$/i =~ message.text and $last_author
      bot.api.send_message(
          chat_id: -1001103213994,
        parse_mode: 'markdown',
        text: slogan($last_author)
      )
      elsif canzomatch
        query = canzomatch[1]
        bot.api.send_chat_action(
          chat_id: message.chat.id,
          action: 'upload_document'
        )
        songpath = `./song.sh '#{query.gsub(/'/,'\\\'')}'`
        songname = `./songname.sh '#{query.gsub(/'/,'\\\'')}'`
        if songpath == 'KO'
          rispondi(bot, message, "Non ci riesco.")
        else
          #bot.api.send_document(
          #  chat_id: message.chat.id,
          #  reply_to_message_id: message.message_id,
          #  document: Faraday::UploadIO.new(File.new(songpath), "audio/mpeg", songname)
          #)
          bot.api.send_audio(
            chat_id: message.chat.id,
            reply_to_message_id: message.message_id,
            audio: Faraday::UploadIO.new(File.new(songpath), "audio/mpeg", songname)
          )
        end
      elsif bullshitmatch
        val = bullshitmatch[1]
        eur = bullshit_in_euro(val)
        if eur != 0
          rispondi(bot, message, "#{val} bullshit corrispondono a #{eur} pregiati euro.")
        else
          rispondi(bot, message, "Non ci riesco.")
        end
      elsif wikimatch
        query = wikimatch[1]
        puts "itwiki(#{query})"
        url = URI.parse("https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=#{CGI.escape(query)}")
        puts url
        response = Net::HTTP.get_response(url)
        first = JSON.parse(response.body)[1][0]
        if first
          puts first
          url = URI.parse("https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=#{CGI.escape(first)}")
          puts url
          response = Net::HTTP.get_response(url)
          obj = JSON.parse(response.body)
          puts obj
          pages = obj['query']['pages']
          pages.each { |k,v|
            testo = v['extract']
            rispondi(bot, message, "#{testo}\nhttps://it.wikipedia.org/wiki/#{URI.encode(v['title'])}")
          }
        else
          rispondi(bot, message, "Non c'è.")
        end
      elsif googlematch
        query = googlematch[1]
        rispondi(bot, message, "Cercatelo con [google](https://www.google.com/search?q=#{URI.encode(query)}) ritardato!™")
      end
    rescue Exception => e
      puts "Eccezione elaborando il messaggio:"
      puts "#{message.text}"
      puts "Errore: #{e}"
    end
  end
end

