// ITEMS

function items_clear(div_items) {
    
    while (div_items.firstChild) {
        div_items.removeChild(div_items.lastChild);
    }
}

function items_populate_makeitem_row(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function items_populate_makeitem(item) {

    if (item.book != null) { item = item.book; }
    
    let a = document.createElement("a");
    a.className = "item_link";
    a.href = "/items/" + item.id;


    let span = document.createElement("span");
    span.className = "item";
    
    let img = document.createElement("img");
    img.src = item.coverPhoto;
    span.append(img);

    let table = document.createElement("table");
    table.append(items_populate_makeitem_row("TITLE: ",        item.title));
    table.append(items_populate_makeitem_row("RELEASE DATE: ", item.releaseDate));
    table.append(items_populate_makeitem_row("DESCRIPTION: ",  item.description));
    table.append(items_populate_makeitem_row("NUM OF PAGES: ", item.numOfPages));
    table.append(items_populate_makeitem_row("ISBN: ",         item.isbn));
    table.append(items_populate_makeitem_row("RATING: ",       item.rating));
    table.append(items_populate_makeitem_row("GENRES: ",       item.genres));
    span.append(table);
    
    a.append(span);
    return a;
}

function items_populate(div_items, items) {

    for (let i = 0; i < items.length; i++) {
        div_items.append(items_populate_makeitem(items[i]));
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
