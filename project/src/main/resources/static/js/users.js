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
    a.href = "/users/" + json.id;

    let span = document.createElement("span");
    span.className = "item";
    
    let table = document.createElement("table");
    table.append(makeTableRow("FIRST NAME: ",    json.firstName));
    table.append(makeTableRow("LAST NAME: ",     json.lastName));
    table.append(makeTableRow("USERNAME: ",      json.username));
    table.append(makeTableRow("MAIL: ",          json.mailAddress));
    table.append(makeTableRow("DATE OF BIRTH: ", json.dateOfBirth));
    table.append(makeTableRow("DESCRITPION: ",   json.description));
    table.append(makeTableRow("ACCOUNT ROLE: ",  json.accountRole));

    span.append(table);
    let img = document.createElement("img");
    img.src = json.profilePicture;
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

    const response = await fetch("/api/users");
    const jsonData = await response.json();
    div_items_populate(jsonData);
}

loaditems();

