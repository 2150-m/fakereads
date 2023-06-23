async function addGenre() {

    let v_genre = document.getElementById("txt_genre").value;

    const response = await fetch('/api/admin/addgenre', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: v_genre,
        })
    });

    const text = await response.text();
    console.log(response);
    console.log(text);
    document.getElementById("p_status").innerHTML = text;
}


let btn = document.getElementById("btn_add");
btn.onclick = addGenre;