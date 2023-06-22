function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}

function makeReview(json) {
    let table = document.getElementById("activations");
    table.append(makeTableRow("RATING",  json.rating));
    
    return table;
}


