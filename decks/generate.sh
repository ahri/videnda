#!/bin/bash

html_header() {
        echo <<HTML
<html>
  <head>
    <title>Deck Listing</title>
  </head>
  <body>
    <ul>
HTML
}

html_content() {
        c="$1"
        printf '<li><a href="%s">%s</a></li>\n' "$c" "$(echo $c | sed 's/\.[^\.]*$//')"
}

html_footer() {
        echo <<HTML
</body>
HTML
}

for f in *.zip; do
       printf '%s %s\n' "$(md5sum "$f" | sed 's/ .*$//')" "$f"
done > list

( html_header
for f in *.zip; do
        html_content "$f"
done
html_footer ) > index.html
