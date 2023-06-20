

let btn = document.getElementById("btn_search");

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

function makeItem(json) {

    let a = document.createElement("a");
    a.className = "item_link";
    a.href = "/items/" + json.id;


    let span = document.createElement("span");
    span.className = "item";
    
    let table = document.createElement("table");
    table.append(makeTableRow("TITLE: ",        json.book.title));
    //table.append(makeTableRow("PHOTO",          json.coverPhoto));
    table.append(makeTableRow("RELEASE DATE: ", json.book.releaseDate));
    table.append(makeTableRow("DESCRIPTION: ",  json.book.description));
    table.append(makeTableRow("NUM OF PAGES: ", json.book.numOfPages));
    table.append(makeTableRow("ISBN: ",         json.book.isbn));
    table.append(makeTableRow("RATING: ",       json.book.rating));
    table.append(makeTableRow("GENRES: ",       json.book.genres));
    span.append(table);
    let img = document.createElement("img");
    img.src = json.book.coverPhoto;
    span.append(img);


    a.append(span);
    return a;
}

function div_items_populate(jsonData) {

    for (let i = 0; i < jsonData.length; i++) {
        div_items.append(makeItem(jsonData[i]));
    }
}

async function loaditems() {
    div_items_clear();

    const response = await fetch("/api/items");
    const jsonData = await response.json();
    div_items_populate(jsonData);
}

loaditems();



// SEARCH
let txt_search = document.getElementById("txt_search");
let btn_search = document.getElementById("btn_search");

async function searchItem(search) {

    div_items_clear();
    const response = await fetch("/api/items/search=" + search);
    const jsonData = await response.json();
    div_items_populate(jsonData);
}

function search() {
    searchItem(txt_search.value);
}

txt_search.addEventListener("keydown", function(event) { if (event.key == 'Enter') { search(); } }, false);
btn_search.onclick = function() { search(); }



