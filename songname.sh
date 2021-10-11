#!/usr/bin/env sh
echo -n '~/tgbot/'
youtube-dl --restrict-filenames --extract-audio --audio-format mp3 --output "songs/%(title)s.mp3" --get-filename "ytsearch1:$1" 2>&1