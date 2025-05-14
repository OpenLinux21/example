function showSuccessAnimation() {
    const animation = document.createElement('div');
    animation.className = 'success-animation';
    animation.innerHTML = `
        <svg class="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
            <circle class="checkmark__circle" cx="26" cy="26" r="25" fill="none"/>
            <path class="checkmark__check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
        </svg>
        <div class="message">注册成功！</div>
    `;
    document.body.appendChild(animation);

    // 2秒后开始淡出动画
    setTimeout(() => {
        animation.classList.add('fade-out');
        // 等待淡出动画完成后跳转
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 500);
    }, 2000);
}

function validateRegister() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('confirm-password').value.trim();

    if (!username || !password || !confirmPassword) {
        alert('请填写所有字段！');
        return;
    }

    if (password !== confirmPassword) {
        alert('两次输入的密码不一致！');
        return;
    }

    // 显示成功动画，之后会自动跳转到登录页面
    showSuccessAnimation();
}

function validateLogin() {
    const username = document.getElementById('login-username').value.trim();
    const password = document.getElementById('login-password').value.trim();

    if (!username || !password) {
        alert('请填写所有字段！');
        return;
    }

    // 这里是演示，所以任何非空输入都视为登录成功
    window.location.href = 'ok.html';
}

// 添加输入动画效果
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });
        input.addEventListener('blur', function() {
            if (this.value === '') {
                this.parentElement.classList.remove('focused');
            }
        });
    });
});