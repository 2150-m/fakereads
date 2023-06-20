let item_id = document.getElementById("item_id");

function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function makeItem(json) {
    let span = document.createElement("span");
    span.className = "item";
    let table = document.createElement("table");
    table.append(makeTableRow("TITLE: ",        json.title));
    //table.append(makeTableRow("PHOTO",          json.coverPhoto));
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
    return span;
}

async function getItem() {
    const response = await fetch("/api/items/" + item_id.value);
    const jsonData = await response.json();
    console.log(jsonData);
    document.body.append(makeItem(jsonData.book))
}

getItem();