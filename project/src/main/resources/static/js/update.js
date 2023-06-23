



async function update() {

    let v_firstName      = document.getElementById("txt_firstName").value;
    let v_lastName       = document.getElementById("txt_lastName").value;
    let v_username       = document.getElementById("txt_username").value;
    let v_mailAddress    = document.getElementById("txt_mailAddress").value;
    let v_password       = document.getElementById("txt_password").value;
    let v_newPassword    = document.getElementById("txt_newPassword").value;
    let v_dateOfBirth    = document.getElementById("txt_dateOfBirth").value;
    //let v_profilePicture = document.getElementById("txt_profilePicture").value;
    let v_description    = document.getElementById("txt_description").value;


    let p_status = document.getElementById("p_status");

    const response = await fetch('/api/myaccount/update', {
        method: 'PUT',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: v_firstName,
            lastName: v_lastName,
            username: v_username,
            mailAddress: v_mailAddress,
            password: v_password,
            newPassword: v_newPassword,
            dateOfBirth: v_dateOfBirth,
            //profilePicture: v_profilePicture,
            description: v_description,
        })
    });

    console.log(response);

    const text = await response.text();
    console.log(text);
    p_status.innerHTML = text;

    if (response.ok) {
        window.location.href = "myaccount";
    }
}

async function loadinfo() {

    const response = await fetch("/api/myaccount");

    if (response.ok) {
        const json = await response.json();

        // load account values
        document.getElementById("txt_firstName").value   = json.firstName;
        document.getElementById("txt_lastName").value    = json.lastName;
        document.getElementById("txt_username").value    = json.username;
        document.getElementById("txt_dateOfBirth").value = json.dateOfBirth;
        document.getElementById("txt_description").value = json.description;
        document.getElementById("txt_mailAddress").placeholder = json.mailAddress;
        //document.getElementById("txt_curpassword").innerHTML;
        //ocument.getElementById("txt_newpassword").innerHTML;
    }

}

loadinfo();

let btn = document.getElementById("btn_update");
btn.onclick = update;