function calculateCalories() {
    const activity = document.getElementById("activity").value;
    const minutes = Number(document.getElementById("minutes").value);
    const intensity = Number(document.getElementById("intensity").value);
    const result = document.getElementById("result");

    if (minutes <= 0 || isNaN(minutes)) {
        result.style.color = "red";
        result.innerText = "Please enter a valid duration.";
        return;
    }

    let baseRate;

    switch (activity) {
        case "walking":
            baseRate = 4;
            break;
        case "running":
            baseRate = 8;
            break;
        case "cycling":
            baseRate = 6;
            break;
    }

    const calories = Math.round(minutes * baseRate * intensity);

    result.style.color = "green";
    result.innerText = `You burned approximately ${calories} calories.`;
}
