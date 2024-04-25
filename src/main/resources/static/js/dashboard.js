document.addEventListener('DOMContentLoaded', () => {
 
  // Both charts
  let labelsInnerHTML = document.getElementById("chartLabels").innerHTML;
  let labels = labelsInnerHTML.replace("[", "").replace("]", "").split(",");

  // Bank Account Chart
  let ctxBankAccount = document.getElementById('bankAccountChart').getContext('2d');

  let bankAccountData = JSON.parse(document.getElementById('bankAccountChartLines').innerHTML);
  
  let bankAccountChart = new Chart(ctxBankAccount, {
    type: 'line',
    data: {
        labels: labels,
        datasets: bankAccountData
    }
  });
  
   // Credit Card Chart
  let ctxCreditCard = document.getElementById('creditCardChart').getContext('2d');
  
  let creditCardData = JSON.parse(document.getElementById('creditCardChartLines').innerHTML);
  
  let creditCardChart = new Chart(ctxCreditCard, {
    type: 'line',
    data: {
        labels: labels,
        datasets: creditCardData
    }
  });

  // Compute totals
  let totalBalance = bankAccountBalancesData.reduce((acc, balance) => acc + balance, 0);
  let totalUnpaidBills = unpaidBillsData.reduce((acc, bill) => acc + bill, 0);

  // Add totals to tables
  let formatter = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' });
  
  // Add total balance to bank accounts table
  //document.querySelector('.bank-accounts-table tfoot th:nth-child(2)').innerText = formatter.format(totalBalance);
    
  // Add total unpaid bills to credit cards table
  //document.querySelector('.credit-cards-table tfoot td:last-child').innerText = formatter.format(totalUnpaidBills);
});


