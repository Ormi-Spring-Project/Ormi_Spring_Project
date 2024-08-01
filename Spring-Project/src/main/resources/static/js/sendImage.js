document.getElementById('write').addEventListener('submit', function (event) {
    event.preventDefault();

    const formData = new FormData();
    const fileInput = document.getElementById('file');

    formData.append('file', fileInput.files[0]);

    fetch('http://localhost:8080/upload', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
        })
        .then(error => {
            console.error('Error:', error);
        });
});

function preview(input) {
    const icon = document.querySelector('.icon-img')

    input.addEventListener('change', () => {
        const reader = new FileReader();
        reader.onload = function (e) {
            icon.src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    })

}