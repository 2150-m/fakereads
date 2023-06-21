let user_id = document.getElementById("user_id");

function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

async function getUserAndShelves() {
    const response = await fetch("/api/users/" + user_id.value);
    const json = await response.json();
    console.log(json);
    
    let user = document.getElementById("user");

    let img = document.createElement("img");
    img.src = json.profilePicture;
    
    let imgcont = document.createElement("div");
    imgcont.id = "imgcont";
    imgcont.append(img);

    user.append(imgcont);

    let table = document.createElement("table");
    table.append(makeTableRow("FIRST NAME: ",    json.firstName));
    table.append(makeTableRow("LAST NAME: ",     json.lastName));
    table.append(makeTableRow("USERNAME: ",      json.username));
    table.append(makeTableRow("MAIL: ",          json.mailAddress));
    table.append(makeTableRow("DATE OF BIRTH: ", json.dateOfBirth));
    table.append(makeTableRow("DESCRITPION: ",   json.description));
    table.append(makeTableRow("ACCOUNT ROLE: ",  json.accountRole));
    user.append(table);

    

    // TODO: display shelves
    let shelves = document.getElementById("shelves");
    
    for (let i = 0; i < json.shelves.length; i++) {
        let shelf = document.createElement("div");
        shelf.className = "shelf";
        let p = document.createElement("p");
        p.innerHTML = json.shelves[i].name;
        shelf.append(p);
        items_populate(shelf, json.shelves[i].shelfItems);
        shelves.append(shelf);
    }
    

    
}

getUserAndShelves();