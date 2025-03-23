document.getElementById('deliveryForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const city = document.getElementById('city').value;
    const vehicleType = document.getElementById('vehicleType').value;

    fetch(`/api/delivery/fee?city=${city}&vehicleType=${vehicleType}`)
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('result').innerText = `Delivery Fee: ${data.fee} €`;
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('result').innerText = error.message;
        });
});