

let btn = document.getElementById("btn_search");

let div_books = document.getElementById("div_books");

function makeTableRow(clm1, clm2) {
    let tr = document.createElement("tr");
    let th = document.createElement("th"); th.innerHTML = clm1; tr.append(th);
    let td = document.createElement("td"); td.innerHTML = clm2; tr.append(td);
    return tr
}



async function loadbooks() {
    const response = await fetch("/api/books");
    const jsonData = await response.json();
    


    for (let i = 0; i < jsonData.length; i++) {
        let obj = jsonData[i];



        let span = document.createElement("span");
        span.className = "book";

        // TABLE
        let table = document.createElement("table");

        let img = document.createElement("img");
        img.src = obj.coverPhoto;

        table.append(makeTableRow("TITLE: ",        obj.title));
        table.append(makeTableRow("PHOTO",          obj.coverPhoto));
        table.append(makeTableRow("RELEASE DATE: ", obj.releaseDate));
        table.append(makeTableRow("DESC: ",         obj.description));
        table.append(makeTableRow("NUM OF PAGES: ", obj.numOfPages));
        table.append(makeTableRow("ISBN: ",         obj.isbn));
        table.append(makeTableRow("RATING: ",       obj.rating));
        table.append(makeTableRow("GENRES: ",       obj.genres));
        span.append(table);
        span.append(img);
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