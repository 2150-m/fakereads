async function api_books() {
    const response = await fetch("/api/books");
    const jsonData = await response.json();
    console.log(jsonData);
}

let btn = document.getElementById("btn_search");



btn.onclick = function () {
    api_books();
};