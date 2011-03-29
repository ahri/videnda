#!/bin/bash

json_header() {
        echo '{'
}

json_content() {
        c="$1"
        echo "\"$c\","
}

json_footer() {
        echo '}'
}

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
        printf '<li><a href="%s">%s</a></li>' "$c" "$(echo $c | sed 's/\.[^\.]*$//')"
}

html_footer() {
        echo <<HTML
</body>
HTML
}



( json_header
for f in *.zip; do
        json_content "$f"
done
json_footer ) > list.json

( html_header
for f in *.zip; do
        html_content "$f"
done
html_footer ) > list.html
