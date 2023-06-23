let user_id = document.getElementById("user_id").value;


async function sendActivation() {


    let mail = document.getElementById("form_activation_mail").value;
    let phone = document.getElementById("form_activation_phone").value;
    let msg = document.getElementById("form_activation_msg").value;


    const response = await fetch('/api/sendactivation/' + user_id, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({mailAddress: mail, phoneNumber: phone, message: msg})
    });


    if (response.ok) {
        const text = await response.text();
        document.getElementById("form_activation_status").innerHTML = text;

        setTimeout(function() {
            document.getElementById("form_activation_popup").style.display = "none";
        }, 2000);
    }
}

function displayUser(json) {
    document.getElementById("user_firstName").innerHTML   = json.firstName;
    document.getElementById("user_lastName").innerHTML    = json.lastName;
    document.getElementById("user_username").innerHTML    = json.username;
    document.getElementById("user_mailAddress").innerHTML = json.mailAddress;
    document.getElementById("user_dateOfBirth").innerHTML = json.dateOfBirth;
    document.getElementById("user_description").innerHTML = json.description;
    document.getElementById("user_accountRole").innerHTML = json.accountRole;
    document.getElementById("user_profilePicture").src    = json.profilePicture;

    if (json.accountRole == "AUTHOR") {

        let cont = document.getElementById("user_activate_cont");

        let btn = document.getElementById("user_activate");
        btn.onclick = function() {
            form_activation_popup.style.display = "block";
            cont.style.display = "none";

            let b = document.getElementById("form_activation_btn");
            b.onclick = function() {
                sendActivation();


                // document.getElementById("form_activation_popup").style.display = "none";
            }
        }

        // show button cont
        cont.style.display = "block";
    }
}

async function addshelf() {

    let txt = document.getElementById("addshelf_txt");
    let status = document.getElementById("addshelf_status");
    
    const response = await fetch('/api/myaccount/shelves/add', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: txt.value})
    });


    const text = await response.text();

    console.log(response);
    console.log(text);
    status.innerHTML = text;

}

function displayUserControls() {

    // hook button with function
    let btn = document.getElementById("addshelf_btn");        
    btn.onclick = addshelf;

    // display addshelf cont
    let user_controls = document.getElementById("addshelf");
    user_controls.style.display = "block"; // TODO: add none by default
}

async function removeShelf(id, div) {

    const response = await fetch('/api/myaccount/shelves/remove/' + id, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
    });

    const text = await response.text();

    console.log(text);
    if (response.ok) {  div.parentNode.removeChild(div); }
}

function displayShelves(json, loggedIn = false) {
    let shelves = document.getElementById("shelves");
    for (let i = 0; i < json.shelves.length; i++) {
        let shelf = document.createElement("div");
        shelf.className = "shelf";

        if (!json.shelves[i].primary) {
            let btn_remove  = document.createElement("button");
            btn_remove.innerHTML = "-";
            btn_remove.onclick = function() { removeShelf(json.shelves[i].id, shelf) }
            shelf.append(btn_remove);
        }

        let p = document.createElement("p");
        p.innerHTML = json.shelves[i].name;
        shelf.append(p);

        if (loggedIn) { items_populate(shelf, json.shelves[i].shelfItems, json.shelves[i].id); }
        else          { items_populate(shelf, json.shelves[i].shelfItems); }
        shelves.append(shelf);
    }
}

async function loadUser() {
    const response_users_id = await fetch("/api/users/" + user_id);
    const response_users_id_json = await response_users_id.json();
    console.log(response_users_id_json);
    
    // display user
    displayUser(response_users_id_json);


    const response_myaccount = await fetch("/api/myaccount");

    if (response_myaccount.ok) {
        displayUserControls();

        // const response_myaccount = await response_myaccount.json()

        // display shelves for myaccount
        displayShelves(response_users_id_json, true);
    }
    else {


        // display shelves for user
        displayShelves(response_users_id_json);
    }


    
}

loadUser();