function calculateCalories() {
    let activity = document.getElementById("activity").value;
    let minutes = document.getElementById("minutes").value;
    let calories = 0;

    if (activity === "walking") {
        calories = minutes * 4;
    } else if (activity === "running") {
        calories = minutes * 8;
    }

    document.getElementById("result").innerText =
        "Calories burned: " + calories;
}
