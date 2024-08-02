// document.getElementById('write').addEventListener('submit', function (event) {
//     event.preventDefault();
//
//     const formData = new FormData();
//     const fileInput = document.getElementById('file');
//
//     formData.append('file', fileInput.files[0]);
//
//     fetch('http://localhost:8080/v1/posts/write', {
//         method: 'POST',
//         body: formData
//     })
//         .then(response => response.json())
//         .then(data => {
//             console.log('Success:', data);
//         })
//         .then(error => {
//             console.error('Error:', error);
//         });
// });

document.getElementById('file').addEventListener('change', function (event) {
    const file = event.target.files[0];
    const preview = document.getElementById('preview');

    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            console.log("File loaded");
            preview.src = e.target.result;
            preview.style.display = 'block';
        }

        reader.readAsDataURL(file);
    } else {
        console.log("No file selected");
        preview.style.display = 'none';
        preview.src = '';
    }
});