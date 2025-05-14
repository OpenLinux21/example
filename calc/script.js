class Calculator {
    constructor() {
        this.previousOperandElement = document.querySelector('.previous-operand');
        this.currentOperandElement = document.querySelector('.current-operand');
        this.historyList = document.querySelector('.history-list');
        this.clear();
        this.loadHistory();
    }

    clear() {
        this.currentOperand = '0';
        this.previousOperand = '';
        this.operation = undefined;
        this.updateDisplay();
    }

    delete() {
        if (this.currentOperand === '0') return;
        this.currentOperand = this.currentOperand.toString().slice(0, -1);
        if (this.currentOperand === '') this.currentOperand = '0';
        this.updateDisplay();
    }

    appendNumber(number) {
        if (number === '.' && this.currentOperand.includes('.')) return;
        if (this.currentOperand === '0' && number !== '.') {
            this.currentOperand = number;
        } else {
            this.currentOperand = this.currentOperand.toString() + number;
        }
        this.updateDisplay();
    }

    chooseOperation(operation) {
        if (this.currentOperand === '') return;
        if (this.previousOperand !== '') {
            this.compute();
        }
        this.operation = operation;
        this.previousOperand = this.currentOperand;
        this.currentOperand = '0';
        this.updateDisplay();
    }

    compute() {
        let computation;
        const prev = parseFloat(this.previousOperand);
        const current = parseFloat(this.currentOperand);
        if (isNaN(prev) || isNaN(current)) return;

        switch (this.operation) {
            case '+':
                computation = prev + current;
                break;
            case '-':
                computation = prev - current;
                break;
            case '×':
                computation = prev * current;
                break;
            case '÷':
                if (current === 0) {
                    alert('不能除以0！');
                    return;
                }
                computation = prev / current;
                break;
            case '%':
                computation = prev % current;
                break;
            default:
                return;
        }

        const calculation = `${prev} ${this.operation} ${current} = ${computation}`;
        this.addToHistory(calculation);
        
        this.currentOperand = computation;
        this.operation = undefined;
        this.previousOperand = '';
        this.updateDisplay();
    }

    updateDisplay() {
        this.currentOperandElement.textContent = this.currentOperand;
        if (this.operation != null) {
            this.previousOperandElement.textContent = 
                `${this.previousOperand} ${this.operation}`;
        } else {
            this.previousOperandElement.textContent = '';
        }
    }

    addToHistory(calculation) {
        const historyItem = document.createElement('div');
        historyItem.className = 'history-item';
        historyItem.textContent = calculation;
        this.historyList.insertBefore(historyItem, this.historyList.firstChild);
        this.saveHistory();
    }

    saveHistory() {
        const history = Array.from(this.historyList.children).map(item => item.textContent);
        localStorage.setItem('calculatorHistory', JSON.stringify(history));
    }

    loadHistory() {
        const history = JSON.parse(localStorage.getItem('calculatorHistory')) || [];
        history.forEach(calculation => {
            const historyItem = document.createElement('div');
            historyItem.className = 'history-item';
            historyItem.textContent = calculation;
            this.historyList.appendChild(historyItem);
        });
    }

    clearHistory() {
        this.historyList.innerHTML = '';
        localStorage.removeItem('calculatorHistory');
    }
}

const calculator = new Calculator();

// 数字按钮事件监听
document.querySelectorAll('.number').forEach(button => {
    button.addEventListener('click', () => {
        calculator.appendNumber(button.textContent);
    });
});

// 运算符按钮事件监听
document.querySelectorAll('.operator').forEach(button => {
    button.addEventListener('click', () => {
        const action = button.dataset.action;
        switch (action) {
            case 'clear':
                calculator.clear();
                break;
            case 'delete':
                calculator.delete();
                break;
            case 'equals':
                calculator.compute();
                break;
            default:
                calculator.chooseOperation(button.textContent);
        }
    });
});

// 清除历史记录按钮事件监听
document.querySelector('.clear-history').addEventListener('click', () => {
    calculator.clearHistory();
}); 