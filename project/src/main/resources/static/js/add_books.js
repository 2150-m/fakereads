let iTitle = document.getElementById("title");
let iDescription = document.getElementById("description");
let iReleaseDate = document.getElementById("release_date");
let iISBN = document.getElementById("isbn");
let iNumOfPages = document.getElementById("num_of_pages");
let iPhoto = document.getElementById("photo");
let iGenres = document.getElementById("genres_select");
let btnAdd = document.getElementById("btn_add");
let p_status = document.getElementById("p_status");

async function fetchGenres() {
    const response = await fetch("/api/myaccount");

    if (response.ok) {
        let options = document.getElementById("genres_select");
        
        // const response2 = await fetch("/api/genreswobooks");
        const response2 = await fetch("/api/genres");
        const genres = await response2.json();

        for (let i = 0; i < genres.length; i++) {
            let option = document.createElement("option");
            option.value = genres[i].id;
            option.name = genres[i].name;
            option.innerHTML = genres[i].name;
            options.append(option);
        }
    }
}

async function apiAdd(vTitle, vDescription, vReleaseDate, vISBN, vNumOfPages, vPhoto, vGenres) {
    console.log(vGenres);
    let selected = []; 
    for (let i = 0; i < vGenres.selectedOptions.length; i++) {
        selected.push(vGenres.selectedOptions[i]);
    }
    console.log(selected);

    const response = await fetch('/api/admin/additem', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: vTitle,
            description: vDescription,
            releaseDate: vReleaseDate,
            isbn: vISBN,
            numOfPages: vNumOfPages,
            coverPhoto: vPhoto,
            genres: selected
        })
    });

    const text = await response.text();
    console.log(response);
    console.log(text);
    p_status.innerHTML = text;
}

function add() {
    // let photo = iPhoto.value;
    // if (photo == "") {
    //     photo = "/covers/404.png";
    // }
    let photo = "/covers/404.png";

    apiAdd(iTitle.value, iDescription.value, iReleaseDate.value, iISBN.value, iNumOfPages.value, photo, iGenres);
}

fetchGenres();

btnAdd.onclick = add;