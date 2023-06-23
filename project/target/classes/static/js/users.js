let div_users = document.getElementById("users");


async function load() {
    users_clear(div_users);

    const response = await fetch("/api/users");
    const json = await response.json();

    console.log(json)
    users_populate(div_users, json);
}

load();

