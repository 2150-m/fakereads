// let genres;
let divEditBooks = document.getElementById("edit_books");

async function saveEditedItem(item, idNum) {
    let iTitle = document.getElementById("input" + idNum + "_title").value;
    let iDescription = document.getElementById("input" + idNum + "_description").value;
    let iReleaseDate = document.getElementById("input" + idNum + "_releaseDate").value;
    let iISBN = document.getElementById("input" + idNum + "_isbn").value;
    let iNumOfPages = document.getElementById("input" + idNum + "_numOfPages").value;
    let iGenres = document.getElementById("input" + idNum + "_genres");

    console.log("Genres: ");
    console.log(iGenres);

    let selected = []; 
    for (let i = 0; i < iGenres.selectedOptions.length; i++) {
        selected.push(iGenres.selectedOptions[i]);
    }
    console.log(selected);

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
            genres: selected,
            coverPhoto: item.book.coverPhoto // pic stays the same
        })
    });

    console.log(response);

    const text = await response.text();
    console.log(text);
}

async function edit_CreateSelect() {
    // const response = await fetch("/api/genreswobooks");
    const response = await fetch("/api/genres");
    const genres = await response.json();
    return genres;
}

function edit_CreateItem(labelName, value, idNum, idPart, inputType) {
    let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.innerHTML = labelName;
    tr.append(th);
    let td = document.createElement("td");
    
    if (inputType == "select") {
        let select = document.createElement("select");
        edit_CreateSelect().then(
            function(genres) {
                select.id = "input" + idNum + "_" + idPart;
                select.multiple = "true";

                for (let i = 0; i < genres.length; i++) {
                    let option = document.createElement("option");
                    option.value = genres[i].id;
                    option.innerHTML = genres[i].name;
                    option.name = genres[i].name;
            
                    select.append(option);
                }
            }
        );

        console.log(select);
        td.append(select);
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

function edit_PopulateTable(item, idNum) {
    let i = item;
    if (i.book != null) { i = item.book; }

    let span = document.createElement("span");
    span.className = "item";
    
    let img = document.createElement("img");
    img.src = i.coverPhoto;
    span.append(img);

    let table = document.createElement("table");
    
    table.append(edit_CreateItem("TITLE: ",        i.title, idNum, "title", "text"));
    table.append(edit_CreateItem("RELEASE DATE: ", i.releaseDate, idNum, "releaseDate", "date"));
    table.append(edit_CreateItem("DESCRIPTION: ",  i.description, idNum, "description", "text"));
    table.append(edit_CreateItem("NUM OF PAGES: ", i.numOfPages, idNum, "numOfPages", "number"));
    table.append(edit_CreateItem("ISBN: ",         i.isbn, idNum, "isbn", "text"));
    table.append(edit_CreateItem("GENRES: ",       i.bookGenres, idNum, "genres", "select"));
    span.append(table);

    let button = document.createElement("button");
    button.className = "item_save";
    button.innerHTML = "SAVE";
    button.onclick = function() { saveEditedItem(item, idNum); }

    span.append(button);
    
    return span;
}

async function edit_LoadItems(search = "") {
    items_clear(divEditBooks);

    let url = "/api/items";
    if (search != "") { url = "/api/items/search=" + search; }

    const responseItems = await fetch(url);
    const itemsJson = await responseItems.json();
    
    for (let i = 0; i < itemsJson.length; i++) {
        divEditBooks.append(edit_PopulateTable(itemsJson[i], i));
    }
}

edit_LoadItems();

// SEARCH
let txt_search = document.getElementById("txt_search");
let btn_search = document.getElementById("btn_search");

function search() {
    edit_LoadItems(txt_search.value);
}

txt_search.addEventListener("keydown", function(event) { if (event.key == 'Enter') { search(); } }, false);
btn_search.onclick = function() { search(); }
