let divEditAuthors = document.getElementById("edit_authors");

async function saveEditedItem(item, idNum) {
    let iFN = document.getElementById("input" + idNum + "_firstName").value;
    let iLN = document.getElementById("input" + idNum + "_lastName").value;
    let iUN = document.getElementById("input" + idNum + "_username").value;
    let iMail = document.getElementById("input" + idNum + "_mailAddress").value;
    let iDOB = document.getElementById("input" + idNum + "_dateOfBirth").value;
    let iDesc = document.getElementById("input" + idNum + "_description").value;

    const response = await fetch('/api/admin/update/author/' + item.id, {
        method: 'PUT',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: iFN,
            lastName: iLN,
            username: iUN,
            mailAddress: iMail,
            dateOfBirth: iDOB,
            description: iDesc,
            coverPhoto: item.profilePicture 
        })
    });

    console.log(response);

    const text = await response.text();
    console.log(text);
}

function edit_CreateItem(labelName, value, idNum, idPart, inputType) {
    let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.innerHTML = labelName;
    tr.append(th);
    let td = document.createElement("td");
    
    let input = document.createElement("input");
    input.type = inputType;
    input.id = "input" + idNum + "_" + idPart;
    input.value = value;
    td.append(input);

    tr.append(td);    
    return tr;
}

function edit_PopulateTable(item, idNum) {
    let i = item;
    if (i.book != null) { i = item.book; }

    let span = document.createElement("span");
    span.className = "item";
    
    let img = document.createElement("img");
    img.src = i.profilePicture;
    span.append(img);

    let table = document.createElement("table");
    
    table.append(edit_CreateItem("FIRST NAME: ",    i.firstName, idNum, "firstName", "text"));
    table.append(edit_CreateItem("LAST NAME: ",     i.lastName, idNum, "lastName", "text"));
    table.append(edit_CreateItem("USERNAME: ",      i.username, idNum, "username", "text"));
    table.append(edit_CreateItem("MAIL: ",          i.mailAddress, idNum, "mailAddress", "text"));
    table.append(edit_CreateItem("DATE OF BIRTH: ", i.dateOfBirth, idNum, "dateOfBirth", "date"));
    table.append(edit_CreateItem("DESCRIPTION: ",   i.description, idNum, "description", "text"));
    span.append(table);

    let button = document.createElement("button");
    button.className = "item_save";
    button.innerHTML = "SAVE";
    button.onclick = function() { saveEditedItem(item, idNum); }

    span.append(button);
    
    return span;
}

async function edit_LoadItems(search = "") {
    items_clear(divEditAuthors);

    const responseItems = await fetch("/api/unactivated_authors");
    const itemsJson = await responseItems.json();
    
    for (let i = 0; i < itemsJson.length; i++) {
        console.log(itemsJson[i]);
        divEditAuthors.append(edit_PopulateTable(itemsJson[i], i));
    }
}

edit_LoadItems();