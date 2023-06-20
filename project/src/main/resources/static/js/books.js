

let btn = document.getElementById("btn_search");

let div_books = document.getElementById("div_books");



function div_books_clear() {
    
    while (div_books.firstChild) {
        div_books.removeChild(div_books.lastChild);
    }
}

function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function div_books_populate(jsonData) {

    for (let i = 0; i < jsonData.length; i++) {
        let obj = jsonData[i].book;

        let span = document.createElement("span");
        span.className = "book";

        // TABLE
        let table = document.createElement("table");

        let img = document.createElement("img");
        img.src = obj.coverPhoto;

        table.append(makeTableRow("TITLE: ",        obj.title));
        //table.append(makeTableRow("PHOTO",          obj.coverPhoto));
        table.append(makeTableRow("RELEASE DATE: ", obj.releaseDate));
        table.append(makeTableRow("DESCRIPTION: ",  obj.description));
        table.append(makeTableRow("NUM OF PAGES: ", obj.numOfPages));
        table.append(makeTableRow("ISBN: ",         obj.isbn));
        table.append(makeTableRow("RATING: ",       obj.rating));
        table.append(makeTableRow("GENRES: ",       obj.genres));
        span.append(table);
        span.append(img);
        // span.append(p1);
        // span.append(p2);
        // span.append(p3);
        // span.append(p4);
        // span.append(p5);
        // span.append(p6);
        // span.append(p7);
        // span.append(p8);

        div_books.append(span);
        
        //div_books.append();
    }
}

async function loadbooks() {
    div_books_clear();

    const response = await fetch("/api/items");
    const jsonData = await response.json();
    div_books_populate(jsonData);
}

loadbooks();

// SEARCHING BOOK

let txt_search = document.getElementById("txt_search");
let btn_search = document.getElementById("btn_search");

async function searchBook(search) {

    div_books_clear();
    const response = await fetch("/api/items/search=" + search);
    const jsonData = await response.json();
    div_books_populate(jsonData);
}



function search() {
    searchBook(txt_search.value);
}


txt_search.addEventListener("keydown", function(event) { if (event.key == 'Enter') { search(); } }, false);
btn_search.onclick = function() { search(); }



