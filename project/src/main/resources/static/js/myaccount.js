


function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function makeMyAccount(json) {
    let myaccount = document.getElementById("myaccount");
    let table = document.createElement("table");
    table.append(makeTableRow("FIRST NAME: ",    json.firstName));
    table.append(makeTableRow("LAST NAME: ",     json.lastName));
    table.append(makeTableRow("USERNAME: ",      json.username));
    table.append(makeTableRow("MAIL: ",          json.mailAddress));
    table.append(makeTableRow("DATE OF BIRTH: ", json.dateOfBirth));
    table.append(makeTableRow("DESCRITPION: ",   json.description));
    table.append(makeTableRow("ACCOUNT ROLE: ",  json.accountRole));
    
    // TODO: json.shelves

    let img = document.createElement("img");
    img.src = json.profilePicture;
    
    myaccount.append(table);
    myaccount.append(img);
}

async function api_myaccount() {
    const response = await fetch("/api/myaccount");
    const jsonData = await response.json();
    console.log(jsonData);
    makeMyAccount(jsonData);
}

api_myaccount();