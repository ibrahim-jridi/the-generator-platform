import {Injectable} from '@angular/core';
import * as html2pdf from 'html2pdf.js';
import {TranslatePipe} from "@ngx-translate/core"; // Make sure to import html2pdf library

@Injectable({
  providedIn: 'root'
})
export class ExportService {

  constructor(
    private translatePipe: TranslatePipe,
  ) {
  }

  exportTable(exportType: string, checkedLines: any[], tollTableData: any[], header: string): void {
    switch (exportType) {
      case 'csv':
        this.exportToCsv(checkedLines, tollTableData);
        break;
      case 'pdf':
        this.exportToPdf(checkedLines, tollTableData, header);
        break;
      case 'json':
        this.exportToJSON(checkedLines, tollTableData);
        break;
      default:
        break;
    }
  }


  exportToCsv(checkedLines: any[], tollTableData: any[]) {
    const currentDate = new Date().toISOString().slice(0, 10);
    let selectedData = checkedLines.length > 0 ? checkedLines : tollTableData;
    let csvContent = 'data:text/csv;charset=utf-8,';
    csvContent += this.parseDataToCSV(selectedData);
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', `export_csv_${currentDate}.csv`);
    document.body.appendChild(link);
    link.click();
  }

  exportToJSON(checkedLines: any[], tollTableData: any[]) {
    const currentDate = new Date().toISOString().slice(0, 10);
    let selectedData = checkedLines.length > 0 ? checkedLines : tollTableData;
    const dataStr = JSON.stringify(selectedData);
    const dataUri = 'data:application/json;charset=utf-8,' + encodeURIComponent(dataStr);
    const link = document.createElement('a');
    link.setAttribute('href', dataUri);
    link.setAttribute('download', `export_csv_${currentDate}.json`);
    document.body.appendChild(link);
    link.click();
  }

  exportToPdf(checkedLines: any[], colonnesName: any[], headerText: string) {
    const currentDate = new Date().toISOString().slice(0, 10);

    // Create a new document container element
    const container = document.createElement('div');

    // Create a container for the logo and company contact info
    const logoAndContactContainer = document.createElement('div');
    logoAndContactContainer.style.display = 'flex'; // Align logo and contact info horizontally
    logoAndContactContainer.style.flexDirection = 'column'; // Align vertically
    logoAndContactContainer.style.width = '300px';
    container.appendChild(logoAndContactContainer);

    // Create a container for the logo
    const logoContainer = document.createElement('div');
    logoAndContactContainer.appendChild(logoContainer);

    // Create a new image element for the logo
    const logo = document.createElement('img');
    logo.src = 'assets/images/logo.svg';
    logo.style.width = '75%'; // Adjust the width of the logo as needed
    logo.style.margin = '40px'; // Adjust margin as needed
    logoContainer.appendChild(logo);

    // Create a paragraph for company contact info
    const contactInfo = document.createElement('p');
    contactInfo.innerHTML = `
        <br>
        ${this.translatePipe.transform('users.PHONE')} : +216 71 130 131/ 56<br>
        ${this.translatePipe.transform('users.EMAIL')} : contact@outlook.fr<br>
    `;
    contactInfo.style.marginLeft = '40px'; // Add some margin between logo and contact info
    logoAndContactContainer.appendChild(contactInfo);

// Add header text
    const header = document.createElement('h4');
    header.textContent = headerText;
    header.style.margin = '0 15px'; // Add margin top and bottom to separate from logo and table
    header.style.textAlign = 'center';
    header.style.marginBottom = '40px';
    header.style.fontWeight = 'normal';
    header.style.color = '#0D294E'; // Set header text color to blue
    container.appendChild(header);

// Create a new table element
    const newTable = document.createElement('table');
    newTable.setAttribute('id', 'exportedTable');
    newTable.style.width = 'calc(100% - 40px)'; // Add CSS style for table width with left and right margins
    newTable.style.margin = '0 20px'; // Add CSS style for left and right margins
    newTable.style.borderCollapse = 'collapse'; // Remove border-collapse

// Add table header dynamically
    const headerRow = newTable.insertRow();
    colonnesName.forEach(header => {
      const th = document.createElement('th');
      th.textContent = this.translatePipe.transform(header.colonneName);
      th.style.borderBottom = '1px solid blue'; // Add blue border under the table header
      th.style.padding = '8px'; // Add CSS style for cell padding
      th.style.color = '#0D294E'; // Set header text color to blue
      headerRow.appendChild(th);
    });

    // Add data rows
    checkedLines.forEach(line => {
      const row = newTable.insertRow();
      colonnesName.forEach(header => {
        const cell = row.insertCell();
        // Ensure that the property exists and is not an object before setting the cell's content
        cell.textContent = line[header.colonneNameId] || ''; // Use empty string if property is undefined
        cell.style.padding = '8px'; // Add CSS style for cell padding
      });
    });

    // Append the new table to the container
    container.appendChild(newTable);

    // Export the new table to PDF
    const opt = {
      filename: `export_csv_${currentDate}.pdf`,
      html2canvas: { scale: 2 },
      jsPDF: {
        unit: 'in',
        format: 'letter',
        orientation: 'landscape',
      }
    };

    html2pdf().from(container).set(opt).save();
  }




  parseDataToCSV(data) {
    let csv = '';
    for (const element of data) {
      let row = '';
      for (const index in element) {
        if (row !== '') row += ',';
        row += element[index];
      }
      csv += row + '\r\n';
    }
    return csv;
  }

  exportCalendar(exportType: string, fullCalendarData: any[]): void {
    switch (exportType) {
      case 'csv':
        this.exportCalendarToCsv(fullCalendarData);
        break;
      case 'pdf':
        this.exportCalendarToPdf();
        break;
      case 'json':
        this.exportCalendarToJSON(fullCalendarData);
        break;
      default:
        break;
    }
  }


  exportCalendarToCsv(selectedData: any[]) {
    const currentDate = new Date().toISOString().slice(0, 10);
    let csvContent = 'data:text/csv;charset=utf-8,';
    csvContent += this.parseDataToCSV(selectedData);
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', `export_csv_${currentDate}.csv`);
    document.body.appendChild(link);
    link.click();
  }

  exportCalendarToJSON(selectedData: any[]) {
    const currentDate = new Date().toISOString().slice(0, 10);
    const dataStr = JSON.stringify(selectedData);
    const dataUri = 'data:application/json;charset=utf-8,' + encodeURIComponent(dataStr);
    const link = document.createElement('a');
    link.setAttribute('href', dataUri);
    link.setAttribute('download', `export_json_${currentDate}.json`);
    document.body.appendChild(link);
    link.click();
  }

  exportCalendarToPdf() {
    const currentDate = new Date().toISOString().slice(0, 10);
    const element = document.querySelector('.full-calendar-container');
    const opt = {
      margin: 0.5,
      filename: `export_${currentDate}.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'in', format: 'letter', orientation: 'landscape' }
    };
    html2pdf().from(element).set(opt).save();
  }
}
