let txt_firstname = document.getElementById("txt_firstname");
let txt_lastname = document.getElementById("txt_lastname");
let txt_username = document.getElementById("txt_username");
let txt_mail = document.getElementById("txt_mail");
let txt_password = document.getElementById("txt_password");
let btn_register = document.getElementById("btn_register");
let p_status = document.getElementById("p_status");

async function api_register(v_firstName, v_lastName, v_username, v_mailAddress, v_password) {
    const response = await fetch('/api/register', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: v_firstName,
            lastName: v_lastName,
            username: v_username,
            mailAddress: v_mailAddress,
            password: v_password
        })
    });

    const text = await response.text();

    console.log(response);

    console.log(text);
    p_status.innerHTML = text;

    if (response.ok) {
        window.location.href = "myaccount";
    }
}




function register() {
    let t1 = txt_firstname.value;
    let t2 = txt_lastname.value;
    let t3 = txt_username.value;
    let t4 = txt_mail.value;
    let t5 = txt_password.value;
    api_register(t1, t2, t3, t4, t5);
}


btn_register.onclick = register;


// TODO: auto focus
// txt_username.addEventListener("keydown", function(event) { if (event.key == 'Enter') { txt_password.focus(); } }, false);
// txt_password.addEventListener("keydown", function(event) { if (event.key == 'Enter') { register(); } }, false);




