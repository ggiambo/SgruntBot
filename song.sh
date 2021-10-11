find songs/ -type f -mtime +1 -delete
FILENAME=$(youtube-dl --restrict-filenames --extract-audio --audio-format mp3 --output "songs/%(title)s.%(ext)s" "ytsearch1:$1" 2>&1 | \
        grep Destination | \
        tail -n 1 | \
        sed 's/^.*Destination: //g')
chmod a+r $FILENAME
if [ $? = 0 ]; then
  echo -n `pwd`/
  echo -n $FILENAME
else
  echo -n KO
fi