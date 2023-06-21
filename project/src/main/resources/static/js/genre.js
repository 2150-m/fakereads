let genre_id  = document.getElementById("genre_id");

let div_items = document.getElementById("div_items");

function div_items_clear() {
    
    while (div_items.firstChild) {
        div_items.removeChild(div_items.lastChild);
    }
}

function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function makeBook(json) {

    let a = document.createElement("a");
    a.className = "item_link";
    a.href = "/finditembybook/" + json.id;


    let span = document.createElement("span");
    span.className = "book";
    
    let table = document.createElement("table");
    table.append(makeTableRow("TITLE: ",        json.title));
    table.append(makeTableRow("RELEASE DATE: ", json.releaseDate));
    table.append(makeTableRow("DESCRIPTION: ",  json.description));
    table.append(makeTableRow("NUM OF PAGES: ", json.numOfPages));
    table.append(makeTableRow("ISBN: ",         json.isbn));
    table.append(makeTableRow("RATING: ",       json.rating));
    table.append(makeTableRow("GENRES: ",       json.genres));
    span.append(table);
    let img = document.createElement("img");
    img.src = json.coverPhoto;
    span.append(img);


    a.append(span);
    return a;
}

function div_items_populate(jsonData) {

    for (let i = 0; i < jsonData.length; i++) {
        div_items.append(makeItem(jsonData[i]));
    }
}

async function getGenreAndBooks() {
    const response = await fetch("/api/genres/" + genre_id.value);
    const json = await response.json();
    console.log(json);
    

    let genre = document.getElementById("genre");
    genre.innerHTML = json.name;

    // TODO: fetch books
    let books = document.getElementById("books");

    for (let i = 0; i < json.books.length; i++) {

        books.append(makeBook(json.books[i]));
    }
    
}

getGenreAndBooks();