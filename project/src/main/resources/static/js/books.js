

let btn = document.getElementById("btn_search");

let div_books = document.getElementById("div_books");

function makeTableHeader(clm1, clm2) {
    let tr = document.createElement("tr");
    let th1 = document.createElement("th"); th1.innerHTML = clm1; tr.append(th1);
    let th2 = document.createElement("th"); th2.innerHTML = clm2; tr.append(th2);
    return tr
}

function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let td1 = document.createElement("td"); td1.innerHTML = clm1; tr.append(td1);
    let td2 = document.createElement("td"); td2.innerHTML = clm2; tr.append(td2);
    return tr
}

async function loadbooks() {
    const response = await fetch("/api/books");
    const jsonData = await response.json();
    


    for (let i = 0; i < jsonData.length; i++) {
        let obj = jsonData[i];



        let span = document.createElement("span");
        span.style = "display: inline-block";

        // TABLE
        let table = document.createElement("table");

        table.append(makeTableHeader("STAVKA", "VREDNOST"));
        table.append(makeTableRow("TITLE: ",        obj.title));
        table.append(makeTableRow("PHOTO",          obj.coverPhoto));
        table.append(makeTableRow("RELEASE DATE: ", obj.releaseDate));
        table.append(makeTableRow("DESC: ",         obj.description));
        table.append(makeTableRow("NUM OF PAGES: ", obj.numOfPages));
        table.append(makeTableRow("ISBN: ",         obj.isbn));
        table.append(makeTableRow("RATING: ",       obj.rating));
        table.append(makeTableRow("GENRES: ",       obj.genres));
        span.append(table);
        // span.append(p1);
        // span.append(p2);
        // span.append(p3);
        // span.append(p4);
        // span.append(p5);
        // span.append(p6);
        // span.append(p7);
        // span.append(p8);

        div_books.append(span);
        console.log(obj);
        
        
        //div_books.append();
      }
}

loadbooks();