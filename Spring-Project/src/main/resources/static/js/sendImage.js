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