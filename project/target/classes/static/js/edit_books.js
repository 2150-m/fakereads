let divEditBooks = document.getElementById("edit_books")

async function saveEditedItem(item, idNum) {
    let iTitle = document.getElementById("input" + idNum + "_title").value;
    let iDescription = document.getElementById("input" + idNum + "_description").value;
    let iReleaseDate = document.getElementById("input" + idNum + "_releaseDate").value;
    let iISBN = document.getElementById("input" + idNum + "_isbn").value;
    let iNumOfPages = document.getElementById("input" + idNum + "_numOfPages").value;
    let iGenres = document.getElementById("input" + idNum + "_genres").value;

    const response = await fetch('/api/admin/update/item/' + item.id, {
        method: 'PUT',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: iTitle,
            description: iDescription,
            releaseDate: iReleaseDate,
            isbn: iISBN,
            numOfPages: iNumOfPages,
            // genres: iGenres
        })
    });

    console.log(response);

    const text = await response.text();
    console.log(text);
}

async function editFetchGenres() {
    let select = createElement("select");
    select.multiple = "true";

    const response = await fetch("/api/genres");
    const genres = await response.json();

    for (let i = 0; i < genres.length; i++) {
        let option = document.createElement("option");
        option.value = genres[i].id;
        option.name = genres[i].name;
        option.innerHTML = genres[i].name;
        select.append(option);
    }

    return select;
}

function editCreateItem(labelName, value, idNum, idPart, inputType) {
    let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.innerHTML = labelName;
    tr.append(th);
    let td = document.createElement("td");
    
    if (inputType == "select") {
        td.append(editFetchGenres());
    } else {
        let input = document.createElement("input");
        input.type = inputType;
        input.id = "input" + idNum + "_" + idPart;
        input.value = value;
        td.append(input);
    }

    tr.append(td);    
    return tr;
}

function editPopulateTable(item, idNum) {
    let i = item;
    if (i.book != null) { i = item.book; }

    let span = document.createElement("span");
    span.className = "item";
    
    let img = document.createElement("img");
    img.src = i.coverPhoto;
    span.append(img);

    let table = document.createElement("table");

    
    table.append(editCreateItem("TITLE: ",        i.title, idNum, "title", "text"));
    table.append(editCreateItem("RELEASE DATE: ", i.releaseDate, idNum, "releaseDate", "date"));
    table.append(editCreateItem("DESCRIPTION: ",  i.description, idNum, "description", "text"));
    table.append(editCreateItem("NUM OF PAGES: ", i.numOfPages, idNum, "numOfPages", "number"));
    table.append(editCreateItem("ISBN: ",         i.isbn, idNum, "isbn", "text"));
    table.append(editCreateItem("GENRES: ",       i.bookGenres, idNum, "genres", "text"));
    span.append(table);

    let button = document.createElement("button");
    button.className = "item_save";
    button.innerHTML = "SAVE";
    button.onclick = function() { saveEditedItem(item, idNum); }

    span.append(button);
    
    return span;
}

async function editLoadItems(search = "") {
    items_clear(divEditBooks);

    let url = "/api/items";
    if (search != "") { url = "/api/items/search=" + search; }

    const responseItems = await fetch(url);
    const itemsJson = await responseItems.json();
    
    for (let i = 0; i < itemsJson.length; i++) {
        divEditBooks.append(editPopulateTable(itemsJson[i], i));
    }
}

editLoadItems();

// SEARCH
let txt_search = document.getElementById("txt_search");
let btn_search = document.getElementById("btn_search");

function search() {
    editLoadItems(txt_search.value);
}

txt_search.addEventListener("keydown", function(event) { if (event.key == 'Enter') { search(); } }, false);
btn_search.onclick = function() { search(); }
