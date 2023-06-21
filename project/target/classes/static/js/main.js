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

