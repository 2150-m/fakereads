// ITEMS

function items_clear(div_items) {
    
    while (div_items.firstChild) {
        div_items.removeChild(div_items.lastChild);
    }
}

function items_populate_makeitem_row(clm1, clm2, link = "") {
    let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.innerHTML = clm1;
    tr.append(th);
    let td = document.createElement("td");
    

    if (link != "") {
        let a = document.createElement("a");
        a.innerHTML = clm2;
        a.className = "item_link";
        a.href = link;
        td.append(a);
    }
    else {
        td.innerHTML = clm2;
    }
    tr.append(td);
    return tr
}

function items_populate_makeitem_row_genres(genres) {

    let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.innerHTML = "GENRES:";
    tr.append(th);
    let td = document.createElement("td");

    for (let i = 0; i < genres.length; i++) {
        let a = document.createElement("a");
        a.innerHTML = genres[i].name;
        a.href = "/genres/" + genres[i].id;
        td.append(a)
    }

    tr.append(td);


    return tr;
}

async function removeBookFromShelf(element, shelfId, bookId) {
    const response = await fetch('/api/myaccount/shelves/' + shelfId + '/removebook/' + bookId, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
    });

    const text = await response.text();
    console.log(text);
    if (response.ok) { element.parentNode.removeChild(element); }
}

function items_populate_makeitem(item, shelfId = 0) {

    let i = item;
    if (i.book != null) { i = item.book; }

    let span = document.createElement("span");
    span.className = "item";
    
    let img = document.createElement("img");
    img.src = i.coverPhoto;
    span.append(img);

    let table = document.createElement("table");

    
    table.append(items_populate_makeitem_row("TITLE:",        i.title, "/items/" + item.id));
    table.append(items_populate_makeitem_row("RELEASE DATE:", i.releaseDate));
    table.append(items_populate_makeitem_row("DESCRIPTION:",  i.description));
    table.append(items_populate_makeitem_row("NUM OF PAGES:", i.numOfPages));
    table.append(items_populate_makeitem_row("ISBN:",         i.isbn));
    table.append(items_populate_makeitem_row("RATING:",       i.rating));
    table.append(items_populate_makeitem_row_genres(i.bookGenres));


    
    span.append(table);

    if (shelfId != 0) {
        let button = document.createElement("button");
        button.className = "item_remove";
        button.innerHTML = "-";
        button.onclick = function() { removeBookFromShelf(span, shelfId, item.id) }

        span.append(button);
    }
    
    
    return span;
}

function items_populate(div_items, items, shelfId = 0) {

    for (let i = 0; i < items.length; i++) {
        div_items.append(items_populate_makeitem(items[i], shelfId));
    }
}





// USERS

function users_clear(div_users) {
    
    while (div_users.firstChild) {
        div_users.removeChild(div_users.lastChild);
    }
}

function users_populate_makeitem_row(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function users_populate_makeitem(json) {

    let a = document.createElement("a");
    a.className = "user_link";
    a.href = "/users/" + json.id;

    let span = document.createElement("span");
    span.className = "user";
    
    let img = document.createElement("img");
    img.src = json.profilePicture;
    span.append(img);

    let table = document.createElement("table");
    table.append(users_populate_makeitem_row("FIRST NAME: ",    json.firstName));
    table.append(users_populate_makeitem_row("LAST NAME: ",     json.lastName));
    table.append(users_populate_makeitem_row("USERNAME: ",      json.username));
    table.append(users_populate_makeitem_row("MAIL: ",          json.mailAddress));
    table.append(users_populate_makeitem_row("DATE OF BIRTH: ", json.dateOfBirth));
    table.append(users_populate_makeitem_row("DESCRITPION: ",   json.description));
    table.append(users_populate_makeitem_row("ACCOUNT ROLE: ",  json.accountRole));
    span.append(table);

    a.append(span);
    return a;
}

function users_populate(div_users, items) {

    for (let i = 0; i < items.length; i++) {
        div_users.append(users_populate_makeitem(items[i]));
    }
}
